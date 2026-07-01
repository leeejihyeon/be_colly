package lab.coder.colly.shared.security;

import lab.coder.colly.domain.auth.domain.model.AuthProvider;

/**
 * 외부 인증 제공자에서 검증된 사용자 정보이다.
 *
 * @param provider 인증 제공자
 * @param providerUserId 제공자 사용자 식별자
 * @param email 제공자 이메일
 * @param name 표시 이름
 */
public record AuthSocialProfile(
        AuthProvider provider,
        String providerUserId,
        String email,
        String name
) {
}
