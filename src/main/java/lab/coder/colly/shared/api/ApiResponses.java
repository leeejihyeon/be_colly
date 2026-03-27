package lab.coder.colly.shared.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 컨트롤러 공통 응답 생성을 위한 헬퍼 클래스이다.
 */
public final class ApiResponses {

    /**
     * 인스턴스 생성을 막기 위한 기본 생성자이다.
     */
    private ApiResponses() {
    }

    /**
     * HTTP 200 성공 응답을 생성한다.
     *
     * @param data 응답 데이터
     * @param <T> 응답 데이터 타입
     * @return 공통 성공 응답
     */
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return of(HttpStatus.OK, "OK", "요청이 성공했습니다.", data);
    }

    /**
     * HTTP 201 생성 성공 응답을 생성한다.
     *
     * @param data 응답 데이터
     * @param <T> 응답 데이터 타입
     * @return 공통 생성 성공 응답
     */
    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return of(HttpStatus.CREATED, "CREATED", "리소스가 생성되었습니다.", data);
    }

    /**
     * 상태 코드와 메시지를 지정해 성공 응답을 생성한다.
     *
     * @param status HTTP 상태 코드
     * @param code 비즈니스 응답 코드
     * @param message 응답 메시지
     * @param data 응답 데이터
     * @param <T> 응답 데이터 타입
     * @return 공통 성공 응답
     */
    public static <T> ResponseEntity<ApiResponse<T>> of(
            HttpStatus status,
            String code,
            String message,
            T data
    ) {
        return ResponseEntity.status(status).body(ApiResponse.success(code, message, data));
    }
}
