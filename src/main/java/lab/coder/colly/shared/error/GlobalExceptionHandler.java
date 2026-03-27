package lab.coder.colly.shared.error;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lab.coder.colly.shared.api.ApiErrorResponse;
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
     * @param request HTTP 요청 정보
     * @return 에러 코드/메시지 응답
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> handleDomainException(
        DomainException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = switch (ex.getErrorCode()) {
            case USER_NOT_FOUND, ORDER_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DUPLICATE_EMAIL -> HttpStatus.CONFLICT;
            case INVALID_AMOUNT -> HttpStatus.BAD_REQUEST;
            case POST_NOT_FOUND, JOIN_NOT_FOUND, MAGIC_LINK_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case JOIN_ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case JOIN_NOT_ALLOWED, INVALID_POST_PAYLOAD, AUTH_INVALID_EMAIL, AUTH_INVALID_PROVIDER, AUTH_INVALID_PROVIDER_USER_ID,
                 MAGIC_LINK_EXPIRED, MAGIC_LINK_ALREADY_USED -> HttpStatus.BAD_REQUEST;
            case USER_RESTRICTED -> HttpStatus.FORBIDDEN;
            case FORBIDDEN_ACTION -> HttpStatus.FORBIDDEN;
            case MAGIC_LINK_RATE_LIMITED -> HttpStatus.TOO_MANY_REQUESTS;
        };

        return ResponseEntity.status(status).body(
            ApiErrorResponse.failure(
                ex.getErrorCode().name(),
                ex.getMessage(),
                request.getRequestURI(),
                Map.of(
                    "koLabel", ex.getErrorCode().getKoLabel(),
                    "engLabel", ex.getErrorCode().getEngLabel()
                )
            )
        );
    }

    /**
     * Bean Validation 실패 예외를 HTTP 400 응답으로 변환한다.
     *
     * @param ex 검증 실패 예외
     * @param request HTTP 요청 정보
     * @return 검증 실패 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> Map.of(
                "field", error.getField(),
                "message", error.getDefaultMessage() == null ? "invalid" : error.getDefaultMessage()
            ))
            .collect(Collectors.toList());

        String message = fieldErrors.isEmpty()
            ? "Validation failed"
            : fieldErrors.get(0).get("field") + " " + fieldErrors.get(0).get("message");

        return ResponseEntity.badRequest().body(
            ApiErrorResponse.failure(
                "VALIDATION_ERROR",
                message,
                request.getRequestURI(),
                Map.of("fieldErrors", fieldErrors)
            )
        );
    }

    /**
     * 처리되지 않은 예외를 HTTP 500 응답으로 변환한다.
     *
     * @param ex 처리되지 않은 예외
     * @param request HTTP 요청 정보
     * @return 서버 내부 오류 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
        Exception ex,
        HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ApiErrorResponse.failure(
                "INTERNAL_SERVER_ERROR",
                "예상치 못한 서버 오류가 발생했습니다.",
                request.getRequestURI(),
                Map.of("exception", ex.getClass().getSimpleName())
            )
        );
    }
}
