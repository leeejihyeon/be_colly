package lab.coder.colly.domain.auth.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Google 소셜 로그인 요청 DTO.
 *
 * @param idToken Google ID 토큰
 */
public record GoogleSocialLoginRequest(
        @NotBlank
        String idToken
) {
}
