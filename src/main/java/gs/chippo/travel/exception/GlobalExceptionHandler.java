package gs.chippo.travel.exception;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Log4j2
@RestControllerAdvice  // 전역 예외처리
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /*
     * Developer Custom Exception: 직접 정의한 RestApiException 에러 클래스에 대한 예외 처리
     */
    @ExceptionHandler(RestApiException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(RestApiException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        String message = ex.getMessage();
        return handleExceptionInternal(errorCode, message);
    };

    // handleExceptionInternal() 메소드를 오버라이딩해 응답 커스터마이징
    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode, String message) {
        ErrorResponse error = new ErrorResponse(errorCode, message);
        log.info(error);
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(new ErrorResponse(errorCode, message));
    }
}
