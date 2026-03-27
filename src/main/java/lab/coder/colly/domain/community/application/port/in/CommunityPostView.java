package lab.coder.colly.domain.community.application.port.in;

import java.time.LocalDateTime;
import lab.coder.colly.domain.community.domain.model.JoinPolicy;
import lab.coder.colly.domain.community.domain.model.PostType;

public record CommunityPostView(
    Long id,
    Long authorUserId,
    String cityCode,
    PostType type,
    String content,
    String imageUrl,
    String locationName,
    String destination,
    String meetingPlace,
    LocalDateTime meetingAt,
    Integer maxParticipants,
    JoinPolicy joinPolicy
) {
}
