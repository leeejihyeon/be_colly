package lab.coder.colly.shared.error;

import jakarta.servlet.http.HttpServletRequest;
import lab.coder.colly.shared.api.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 도메인 예외를 HTTP 응답으로 변환한다.
     *
     * @param ex      도메인 예외
     * @param request HTTP 요청 정보
     * @return 에러 코드/메시지 응답
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> handleDomainException(
            DomainException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = switch (ex.getErrorCode()) {
            case USER_NOT_FOUND, ORDER_NOT_FOUND, POST_NOT_FOUND, ACCOMMODATION_NOT_FOUND,
                 STAY_NOT_FOUND, JOIN_NOT_FOUND, MAGIC_LINK_NOT_FOUND ->
                    HttpStatus.NOT_FOUND;
            case DUPLICATE_EMAIL, JOIN_ALREADY_EXISTS, DUPLICATE_STAY -> HttpStatus.CONFLICT;
            case INVALID_AMOUNT, JOIN_NOT_ALLOWED, INVALID_POST_PAYLOAD, INVALID_STAY_DATE_RANGE,
                 AUTH_INVALID_EMAIL, AUTH_INVALID_PROVIDER,
                 AUTH_INVALID_PROVIDER_USER_ID, MAGIC_LINK_EXPIRED, MAGIC_LINK_ALREADY_USED -> HttpStatus.BAD_REQUEST;
            case AUTH_UNAUTHORIZED, AUTH_INVALID_ACCESS_TOKEN, AUTH_SOCIAL_TOKEN_INVALID,
                 REFRESH_TOKEN_NOT_FOUND, REFRESH_TOKEN_EXPIRED -> HttpStatus.UNAUTHORIZED;
            case USER_RESTRICTED, FORBIDDEN_ACTION -> HttpStatus.FORBIDDEN;
            case MAGIC_LINK_RATE_LIMITED -> HttpStatus.TOO_MANY_REQUESTS;
            case MAGIC_LINK_EMAIL_SEND_FAILED -> HttpStatus.SERVICE_UNAVAILABLE;
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
     * @param ex      검증 실패 예외
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
     * @param ex      처리되지 않은 예외
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
