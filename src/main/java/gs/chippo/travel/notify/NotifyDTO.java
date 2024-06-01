package gs.chippo.travel.notify;

import lombok.*;

public class NotifyDTO {
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response {
        String id;
        String name;
        String content;
        String type;
        String createdAt;
        public static Response createResponse(NotifyEntity notify) {
            return Response.builder()
                    .content(notify.getContent())
                    .id(notify.getId().toString())
                    .name(notify.getReceiver().getUsername())
                    .createdAt(notify.getCreatedAt().toString())
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public class Request {
        private Long userId;
        private NotifyEntity.NotificationType notificationType;
        private String content;
        private String url;
    }
}