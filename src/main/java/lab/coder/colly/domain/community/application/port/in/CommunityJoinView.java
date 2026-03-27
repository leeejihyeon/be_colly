package lab.coder.colly.domain.community.application.port.in;

import lab.coder.colly.domain.community.domain.model.JoinStatus;

public record CommunityJoinView(
    Long joinId,
    Long postId,
    Long userId,
    JoinStatus status
) {
}
