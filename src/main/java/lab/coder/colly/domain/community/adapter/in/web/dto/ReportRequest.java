package lab.coder.colly.domain.community.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 사용자 신고 요청 DTO.
 *
 * @param reporterUserId 신고자 사용자 ID
 * @param targetUserId 피신고자 사용자 ID
 * @param reason 신고 사유
 */
public record ReportRequest(
    @NotNull Long reporterUserId,
    @NotNull Long targetUserId,
    @NotBlank String reason
) {
}
