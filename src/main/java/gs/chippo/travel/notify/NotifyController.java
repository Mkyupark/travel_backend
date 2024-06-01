package gs.chippo.travel.notify;

import gs.chippo.travel.user.UserEntity;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@Log4j2
@RestController
@RequestMapping("/api/notify")
public class NotifyController {

    @Autowired
    private NotifyService notifyService;

    @GetMapping(produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal String userId,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notifyService.subscribe(userId, lastEventId);
    }

    @PostMapping("/send")
    public void sendNotification(@AuthenticationPrincipal String userId, @RequestBody NotifyDTO.Request request) {

        notifyService.send(userId, request.getNotificationType(), request.getContent(), request.getUrl());
    }
}
