package lab.coder.colly.domain.community.application.port.out;

import java.util.Optional;
import lab.coder.colly.domain.community.domain.model.CommunityJoin;

public interface CommunityJoinPort {

    CommunityJoin save(CommunityJoin join);

    Optional<CommunityJoin> findJoinById(Long joinId);

    Optional<CommunityJoin> findByPostIdAndUserId(Long postId, Long userId);

    long countApprovedByPostId(Long postId);
}
