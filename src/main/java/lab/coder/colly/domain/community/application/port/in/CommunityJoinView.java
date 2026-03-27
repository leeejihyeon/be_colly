package lab.coder.colly.domain.community.application.port.in;

import lab.coder.colly.domain.community.domain.model.JoinStatus;

/**
 * 커뮤니티 모임 참여 응답 뷰.
 *
 * @param joinId 모임 참여 식별자
 * @param postId 게시글 식별자
 * @param userId 사용자 식별자
 * @param status 참여 상태
 */
public record CommunityJoinView(
    Long joinId,
    Long postId,
    Long userId,
    JoinStatus status
) {
}
