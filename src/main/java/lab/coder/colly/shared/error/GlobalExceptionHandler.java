package lab.coder.colly.shared.error;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 도메인 예외를 HTTP 응답으로 변환한다.
     *
     * @param ex 도메인 예외
     * @return 에러 코드/메시지 응답
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, Object>> handleDomainException(DomainException ex) {
        HttpStatus status = switch (ex.getErrorCode()) {
            case USER_NOT_FOUND, ORDER_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DUPLICATE_EMAIL -> HttpStatus.CONFLICT;
            case INVALID_AMOUNT -> HttpStatus.BAD_REQUEST;
            case POST_NOT_FOUND, JOIN_NOT_FOUND, MAGIC_LINK_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case JOIN_ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case JOIN_NOT_ALLOWED, INVALID_POST_PAYLOAD, AUTH_INVALID_EMAIL, MAGIC_LINK_EXPIRED, MAGIC_LINK_ALREADY_USED -> HttpStatus.BAD_REQUEST;
            case USER_RESTRICTED -> HttpStatus.FORBIDDEN;
            case FORBIDDEN_ACTION -> HttpStatus.FORBIDDEN;
            case MAGIC_LINK_RATE_LIMITED -> HttpStatus.TOO_MANY_REQUESTS;
        };

        return ResponseEntity.status(status).body(Map.of(
            "errorCode", ex.getErrorCode().name(),
            "message", ex.getMessage()
        ));
    }

    /**
     * Bean Validation 실패 예외를 HTTP 400 응답으로 변환한다.
     *
     * @param ex 검증 실패 예외
     * @return 검증 실패 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(error -> error.getField() + " " + error.getDefaultMessage())
            .orElse("Validation failed");

        return ResponseEntity.badRequest().body(Map.of(
            "errorCode", "VALIDATION_ERROR",
            "message", message
        ));
    }
}
