package lab.coder.colly.domain.user.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 사용자 생성 요청 DTO.
 *
 * @param email 사용자 이메일
 * @param name 사용자 이름
 */
public record CreateUserRequest(
    @Email @NotBlank String email,
    @NotBlank String name
) {
}
