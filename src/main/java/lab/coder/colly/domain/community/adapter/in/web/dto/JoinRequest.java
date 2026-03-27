package lab.coder.colly.domain.community.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;

/**
 * 모임 참여 요청 DTO.
 *
 * @param userId 참여 사용자 ID
 */
public record JoinRequest(@NotNull Long userId) {
}
