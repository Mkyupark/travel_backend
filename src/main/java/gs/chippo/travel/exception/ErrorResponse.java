package gs.chippo.travel.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int statusCode;
    private final String error;
    private final String statusMessage;
    private final String serverMessage;

    public ErrorResponse(ErrorCode errorCode,String message) {
        this.statusCode = errorCode.getHttpStatus().value();
        this.error = errorCode.getHttpStatus().name();
        this.statusMessage = errorCode.getMessage();
        this.serverMessage = message;
    }
}
