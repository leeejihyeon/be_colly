package lab.coder.colly.shared.api;

import java.time.LocalDateTime;

/**
 * 성공 응답 공통 포맷이다.
 *
 * @param <T> 응답 데이터 타입
 * @param success 성공 여부
 * @param code 비즈니스 응답 코드
 * @param message 응답 메시지
 * @param data 응답 데이터
 * @param timestamp 응답 시각
 */
public record ApiResponse<T>(
        boolean success,
        String code,
        String message,
        T data,
        LocalDateTime timestamp
) {

    /**
     * 성공 응답 객체를 생성한다.
     *
     * @param code 비즈니스 응답 코드
     * @param message 응답 메시지
     * @param data 응답 데이터
     * @param <T> 응답 데이터 타입
     * @return 성공 응답 객체
     */
    public static <T> ApiResponse<T> success(
            String code,
            String message,
            T data
    ) {
        return new ApiResponse<>(
                true,
                code,
                message,
                data,
                LocalDateTime.now()
        );
    }
}
