package lab.coder.colly.domain.auth.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 로그인 세션(리프레시 토큰) 정보를 표현하는 도메인 모델.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthSession {

    private final Long id;
    private final Long userId;
    private final String refreshTokenHash;
    private final LocalDateTime expiresAt;

    /**
     * 신규 인증 세션 정보를 생성한다.
     *
     * @param userId           사용자 식별자
     * @param refreshTokenHash 리프레시 토큰 해시
     * @param expiresAt        세션 만료 시각
     * @return 신규 인증 세션 정보
     */
    public static AuthSession create(
            Long userId,
            String refreshTokenHash,
            LocalDateTime expiresAt
    ) {
        return new AuthSession(
                null,
                userId,
                refreshTokenHash,
                expiresAt
        );
    }

    /**
     * 저장된 인증 세션 정보를 복원한다.
     *
     * @param id               식별자
     * @param userId           사용자 식별자
     * @param refreshTokenHash 리프레시 토큰 해시
     * @param expiresAt        세션 만료 시각
     * @return 복원된 인증 세션 정보
     */
    public static AuthSession restore(
            Long id,
            Long userId,
            String refreshTokenHash,
            LocalDateTime expiresAt
    ) {
        return new AuthSession(
                id,
                userId,
                refreshTokenHash,
                expiresAt
        );
    }
}
