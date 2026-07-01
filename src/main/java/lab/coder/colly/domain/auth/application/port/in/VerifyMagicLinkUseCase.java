package lab.coder.colly.domain.auth.application.port.in;

import lab.coder.colly.domain.auth.domain.model.AuthProvider;

/**
 * 매직링크 검증 유스케이스.
 */
public interface VerifyMagicLinkUseCase {

    /**
     * 매직링크 토큰을 검증하고 로그인 처리한다.
     *
     * @param command 매직링크 검증 요청 정보
     * @return 로그인 결과
     */
    LoginResult verify(VerifyMagicLinkCommand command);

    /**
     * 매직링크 검증 요청 명령.
     *
     * @param token 매직링크 토큰
     */
    record VerifyMagicLinkCommand(String token) {
    }

    /**
     * 로그인 결과.
     *
     * @param userId       사용자 식별자
     * @param email        사용자 이메일
     * @param accessToken  액세스 토큰
     * @param refreshToken 리프레시 토큰
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
