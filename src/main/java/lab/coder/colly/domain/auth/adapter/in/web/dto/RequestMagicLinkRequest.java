package lab.coder.colly.domain.auth.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 매직링크 발급 요청 DTO.
 *
 * @param email 로그인 이메일 주소
 */
public record RequestMagicLinkRequest(
        @NotBlank @Email String email
) {
}
