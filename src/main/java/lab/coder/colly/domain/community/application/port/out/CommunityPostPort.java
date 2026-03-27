package lab.coder.colly.domain.community.application.port.out;

import java.util.List;
import java.util.Optional;
import lab.coder.colly.domain.community.domain.model.CommunityPost;
import lab.coder.colly.domain.community.domain.model.PostType;

public interface CommunityPostPort {

    CommunityPost save(CommunityPost post);

    Optional<CommunityPost> findPostById(Long postId);

    List<CommunityPost> findByCityCodeAndType(String cityCode, PostType type);
}
