package lab.coder.colly.domain.community.application.port.in;

import lab.coder.colly.domain.community.domain.model.JoinStatus;

/**
 * 모임 참여 승인/거절 유스케이스.
 */
public interface ReviewCommunityJoinUseCase {

    /**
     * 모임 참여 요청을 승인 또는 거절한다.
     *
     * @param command 참여 검토 요청 정보
     * @return 변경된 참여 상태
     */
    CommunityJoinView review(ReviewCommunityJoinCommand command);

    /**
     * 모임 참여 검토 명령.
     *
     * @param joinId     참여 식별자
     * @param hostUserId 모임장 사용자 식별자
     * @param status     변경할 참여 상태
     */
    record ReviewCommunityJoinCommand(
            Long joinId,
            Long hostUserId,
            JoinStatus status
    ) {
    }
}
