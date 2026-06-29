package lab.coder.colly.domain.auth.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 리프레시 토큰 갱신 요청 DTO.
 *
 * @param refreshToken 리프레시 토큰
 */
public record RefreshTokenRequest(
        @NotBlank String refreshToken
) {
}
