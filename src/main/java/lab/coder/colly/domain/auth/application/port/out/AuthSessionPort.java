package lab.coder.colly.domain.auth.application.port.out;

import lab.coder.colly.domain.auth.domain.model.AuthSession;

import java.util.Optional;

/**
 * 인증 세션 저장 아웃바운드 포트.
 */
public interface AuthSessionPort {

    /**
     * 인증 세션을 저장한다.
     *
     * @param session 저장할 인증 세션
     * @return 저장된 인증 세션
     */
    AuthSession save(AuthSession session);

    /**
     * 해시된 리프레시 토큰으로 세션을 조회한다.
     *
     * @param refreshTokenHash 리프레시 토큰 해시
     * @return 조회된 세션
     */
    Optional<AuthSession> findByRefreshTokenHash(String refreshTokenHash);

    /**
     * 특정 리프레시 토큰의 세션을 삭제한다.
     *
     * @param refreshTokenHash 리프레시 토큰 해시
     */
    void deleteByRefreshTokenHash(String refreshTokenHash);

    /**
     * 사용자의 모든 세션을 삭제한다.
     *
     * @param userId 사용자 ID
     */
    void deleteByUserId(Long userId);

    /**
     * 만료된 세션을 삭제한다.
     */
    void deleteExpiredSessions();
}
