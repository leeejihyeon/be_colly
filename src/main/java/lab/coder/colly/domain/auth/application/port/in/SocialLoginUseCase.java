package lab.coder.colly.domain.auth.application.port.in;

import lab.coder.colly.domain.auth.domain.model.AuthProvider;

/**
 * 소셜 로그인/회원가입 유스케이스.
 */
public interface SocialLoginUseCase {

    /**
     * 소셜 제공자 계정으로 로그인 또는 회원가입을 처리한다.
     *
     * @param command 소셜 로그인 요청 정보
     * @return 로그인 결과
     */
    SocialLoginResult login(SocialLoginCommand command);

    /**
     * 소셜 로그인 요청 명령.
     *
     * @param provider       인증 제공자
     * @param providerUserId 제공자 내 사용자 식별자
     * @param email          사용자 이메일
     * @param name           사용자 표시 이름
     */
    record SocialLoginCommand(
            AuthProvider provider,
            String providerUserId,
            String email,
            String name
    ) {
    }

    /**
     * 소셜 로그인 결과.
     *
     * @param userId       사용자 식별자
     * @param email        사용자 이메일
     * @param accessToken  액세스 토큰
     * @param refreshToken 리프레시 토큰
     */
    record SocialLoginResult(
            Long userId,
            String email,
            String accessToken,
            String refreshToken
    ) {
    }
}
