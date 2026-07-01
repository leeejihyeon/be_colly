package lab.coder.colly.domain.auth.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Apple 로그인 요청 DTO.
 *
 * @param identityToken Apple identity token
 * @param authorizationCode Apple authorization code
 * @param name 표시 이름
 */
public record AppleSocialLoginRequest(
        @NotBlank
        String identityToken,
        String authorizationCode,
        String name
) {
}
