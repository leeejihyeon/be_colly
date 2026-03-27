package lab.coder.colly.domain.auth.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 소셜 로그인/회원가입 요청 DTO.
 *
 * @param provider 소셜 제공자(GOOGLE/APPLE/FACEBOOK)
 * @param providerUserId 소셜 제공자 내 사용자 식별자
 * @param email 사용자 이메일
 * @param name 사용자 표시 이름
 */
public record SocialLoginRequest(
        @NotBlank
        String provider,

        @NotBlank
        String providerUserId,

        @NotBlank
        @Email
        String email,

        String name
) {
}
