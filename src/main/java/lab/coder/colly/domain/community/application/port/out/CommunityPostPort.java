package lab.coder.colly.domain.community.application.port.out;

import java.util.List;
import java.util.Optional;
import lab.coder.colly.domain.community.domain.model.CommunityPost;
import lab.coder.colly.domain.community.domain.model.PostType;

/**
 * 커뮤니티 게시글 저장/조회 아웃바운드 포트.
 */
public interface CommunityPostPort {

    /**
     * 커뮤니티 게시글을 저장한다.
     *
     * @param post 저장할 커뮤니티 게시글
     * @return 저장된 커뮤니티 게시글
     */
    CommunityPost save(CommunityPost post);

    /**
     * 게시글 식별자로 커뮤니티 게시글을 조회한다.
     *
     * @param postId 게시글 식별자
     * @return 커뮤니티 게시글 조회 결과
     */
    Optional<CommunityPost> findPostById(Long postId);

    /**
     * 도시/타입 조건으로 커뮤니티 게시글 목록을 조회한다.
     *
     * @param cityCode 도시 코드
     * @param type 게시글 타입(전체 조회 시 null)
     * @return 커뮤니티 게시글 목록
     */
    List<CommunityPost> findByCityCodeAndType(String cityCode, PostType type);
}
