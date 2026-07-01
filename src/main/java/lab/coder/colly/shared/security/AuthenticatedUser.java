package lab.coder.colly.shared.security;

import lab.coder.colly.domain.auth.domain.model.AuthProvider;

/**
 * 인증 필터가 SecurityContext에 저장하는 현재 사용자 정보이다.
 *
 * @param userId 사용자 식별자
 * @param email 사용자 이메일
 * @param provider 현재 로그인 제공자
 * @param sessionId 인증 세션 식별자
 */
public record AuthenticatedUser(
        Long userId,
        String email,
        AuthProvider provider,
        Long sessionId
) {
}
