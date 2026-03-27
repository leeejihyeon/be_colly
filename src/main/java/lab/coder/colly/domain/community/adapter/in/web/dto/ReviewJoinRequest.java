package lab.coder.colly.domain.community.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import lab.coder.colly.domain.community.domain.model.JoinStatus;

/**
 * 모임 참여 승인/거절 요청 DTO.
 *
 * @param hostUserId 모임장 사용자 ID
 * @param status     변경할 참여 상태
 */
public record ReviewJoinRequest(
        @NotNull
        Long hostUserId,

        @NotNull
        JoinStatus status
) {
}
