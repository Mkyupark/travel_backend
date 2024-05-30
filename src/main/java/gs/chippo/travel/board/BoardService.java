package gs.chippo.travel.board;

import gs.chippo.travel.board.BoardDTO;
import gs.chippo.travel.board.BoardEntity;
import gs.chippo.travel.sse.EmitterRepository;
import gs.chippo.travel.user.UserEntity;
import gs.chippo.travel.board.BoardRepository;
import gs.chippo.travel.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BoardService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmitterRepository emitterRepository;


    public List<BoardEntity> create(final String userId, final BoardDTO entity){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        BoardEntity tempEntity = BoardDTO.toEntity(entity);
        tempEntity.setUser(user);
        validate(tempEntity);
        boardRepository.save(tempEntity);
        return boardRepository.findByUserId(userId);
    }


    public void validate(final BoardEntity entity) {
        if(entity == null ) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
        if(entity.getUser() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
        log.info("Entity : {}", entity);
    }

//    // [1] subscribe()
//    public SseEmitter subscribe(String userId, String lastEventId) { // (1-1)
//        String emitterId = makeTimeIncludeId(userId); // (1-2)
//        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT)); // (1-3)
//        // (1-4)
//        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
//        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
//        emitter.onError((e) -> emitterRepository.deleteById(emitterId));
//        // (1-5) 503 에러를 방지하기 위한 더미 이벤트 전송
//        String eventId = makeTimeIncludeId(userId);
//        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + userId + "]");
//        // (1-6) 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
//        if (hasLostData(lastEventId)) {
//            sendLostData(lastEventId, userId, emitterId, emitter);
//        }
//        return emitter; // (1-7)
//    }
//    private String makeTimeIncludeId(String email) { // (3)
//        return email + "_" + System.currentTimeMillis();
//    }
//    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) { // (4)
//        try {
//            emitter.send(SseEmitter.event()
//                    .id(eventId)
//                    .name("sse")
//                    .data(data)
//            );
//        } catch (IOException exception) {
//            emitterRepository.deleteById(emitterId);
//        }
//    }
//
//    private boolean hasLostData(String lastEventId) { // (5)
//    private boolean hasLostData(String lastEventId) { // (5)
//        return !lastEventId.isEmpty();
//    }
//
//    private void sendLostData(String lastEventId, String userEmail, String emitterId, SseEmitter emitter) { // (6)
//        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(userEmail));
//        eventCaches.entrySet().stream()
//                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
//                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
//    }
//


}




