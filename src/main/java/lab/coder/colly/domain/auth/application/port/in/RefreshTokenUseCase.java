package lab.coder.colly.domain.auth.application.port.in;

import lab.coder.colly.domain.auth.domain.model.AuthProvider;

/**
 * 리프레시 토큰 기반 인증 세션 갱신 유스케이스.
 */
public interface RefreshTokenUseCase {

    /**
     * 리프레시 토큰으로 액세스/리프레시 토큰을 갱신한다.
     *
     * @param command 리프레시 요청 정보
     * @return 갱신된 로그인 결과
     */
    LoginResult refresh(RefreshTokenCommand command);

    /**
     * 리프레시 토큰 갱신 요청 정보.
     *
     * @param refreshToken 리프레시 토큰
     */
    record RefreshTokenCommand(String refreshToken) {
    }

    /**
     * 갱신된 로그인 결과.
     *
     * @param userId       사용자 식별자
     * @param email        사용자 이메일
     * @param accessToken  액세스 토큰
     * @param refreshToken 새로 발급된 리프레시 토큰
     * @param expiresInSeconds 액세스 토큰 만료 시간(초)
     * @param refreshExpiresInSeconds 리프레시 토큰 만료 시간(초)
     * @param provider 로그인 인증 제공자
     */
    record LoginResult(
            Long userId,
            String email,
            String accessToken,
            String refreshToken,
            long expiresInSeconds,
            long refreshExpiresInSeconds,
            AuthProvider provider
    ) {
    }
}
