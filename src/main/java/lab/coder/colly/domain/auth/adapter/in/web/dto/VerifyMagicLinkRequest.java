package lab.coder.colly.domain.auth.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 매직링크 검증 요청 DTO.
 *
 * @param token 발급된 매직 토큰
 */
public record VerifyMagicLinkRequest(
        @NotBlank
        String token
) {
}
