package gs.chippo.travel.notify;

import gs.chippo.travel.sse.EmitterRepository;
import gs.chippo.travel.user.UserEntity;
import gs.chippo.travel.user.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
public class NotifyService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    // SSE 연결 지속 시간 설정

    @Autowired
    private EmitterRepository emitterRepository;

    @Autowired
    private NotifyRepository notifyRepository;

    @Autowired
    private UserRepository userRepository;
//⠀
//    public NotifyService(EmitterRepository emitterRepository, NotifyRepository notifyRepository) {
//        this.emitterRepository = emitterRepository;
//        this.notifyRepository = notifyRepository;
//    }
//⠀
    // [1] subscribe()
    public SseEmitter subscribe(String userId, String lastEventId) { // (1-1)
        log.info(userId);
        String emitterId = makeTimeIncludeId(userId); // (1-2)
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT)); // (1-3)
        // (1-4)
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // (1-5) 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(userId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + userId + "]");

        // (1-6) 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }

        return emitter; // (1-7)
    }

    private String makeTimeIncludeId(String email) { // (3)
        return email + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) { // (4)
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data)
            );
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) { // (5)
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, String userEmail, String emitterId, SseEmitter emitter) { // (6)
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(userEmail));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    // [2] send()
    //@Override
    public void send(String userId, NotifyEntity.NotificationType notificationType, String content, String url) {
        log.info(userId);
        Optional<UserEntity> receiverOptional = this.userRepository.findById(userId);
        if (receiverOptional.isPresent()) {
            UserEntity receiver = receiverOptional.get();
            NotifyEntity notification = notifyRepository.save(createNotification(receiver, notificationType, content, url)); // (2-1)

            String receiverEmail = receiver.getEmail(); // (2-2)
            String eventId = receiverEmail + "_" + System.currentTimeMillis(); // (2-3)
            Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverEmail); // (2-4)
            emitters.forEach( // (2-5)
                    (key, emitter) -> {
                        emitterRepository.saveEventCache(key, notification);
                        sendNotification(emitter, eventId, key, NotifyDTO.Response.createResponse(notification));
                    }
            );
        } else {
            // 유저가 존재하지 않는 경우의 처리
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    private NotifyEntity createNotification(UserEntity receiver, NotifyEntity.NotificationType notificationType, String content, String url) { // (7)
        return NotifyEntity.builder()
                .receiver(receiver)
                .notificationType(notificationType)
                .content(content)
                .url(url)
                .isRead(false)
                .build();
    }

}
