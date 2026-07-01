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
    VerifyMagicLinkUseCase.LoginResult login(SocialLoginCommand command);

    /**
     * 소셜 로그인 요청 명령.
     *
     * @param provider 인증 제공자
     * @param idToken 제공자 인증 토큰
     * @param authorizationCode 제공자 인가 코드(선택)
     * @param name 사용자 표시 이름
     */
    record SocialLoginCommand(
            AuthProvider provider,
            String idToken,
            String authorizationCode,
            String name
    ) {
    }
}
