package lab.coder.colly.domain.auth.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 로그아웃 요청 DTO.
 *
 * @param refreshToken 종료할 리프레시 토큰
 */
public record SignOutRequest(
        @NotBlank String refreshToken
) {
}
