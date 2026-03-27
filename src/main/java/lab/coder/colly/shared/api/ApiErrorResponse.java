package lab.coder.colly.shared.api;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 실패 응답 공통 포맷이다.
 *
 * @param success 성공 여부(항상 false)
 * @param code 에러 코드
 * @param message 에러 메시지
 * @param path 요청 경로
 * @param details 상세 에러 정보
 * @param timestamp 응답 시각
 */
public record ApiErrorResponse(
        boolean success,
        String code,
        String message,
        String path,
        Map<String, Object> details,
        LocalDateTime timestamp
) {

    /**
     * 실패 응답 객체를 생성한다.
     *
     * @param code 에러 코드
     * @param message 에러 메시지
     * @param path 요청 경로
     * @param details 상세 정보
     * @return 실패 응답 객체
     */
    public static ApiErrorResponse failure(
            String code,
            String message,
            String path,
            Map<String, Object> details
    ) {
        return new ApiErrorResponse(
                false,
                code,
                message,
                path,
                details,
                LocalDateTime.now()
        );
    }
}
