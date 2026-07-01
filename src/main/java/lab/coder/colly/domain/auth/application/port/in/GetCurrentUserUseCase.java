package lab.coder.colly.domain.auth.application.port.in;

import lab.coder.colly.domain.auth.domain.model.AuthProvider;

/**
 * 현재 인증된 사용자 조회 유스케이스.
 */
public interface GetCurrentUserUseCase {

    /**
     * 현재 로그인 사용자의 기본 인증 정보를 조회한다.
     *
     * @param userId 사용자 식별자
     * @param provider 현재 인증 제공자
     * @return 사용자 정보
     */
    CurrentUserView getCurrentUser(Long userId, AuthProvider provider);

    /**
     * 현재 로그인 사용자 응답.
     *
     * @param userId 사용자 식별자
     * @param email 사용자 이메일
     * @param provider 현재 인증 제공자
     */
    record CurrentUserView(
            Long userId,
            String email,
            AuthProvider provider
    ) {
    }
}
