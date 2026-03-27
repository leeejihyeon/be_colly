package lab.coder.colly.domain.community.application.port.in;

import java.util.List;
import lab.coder.colly.domain.community.domain.model.PostType;

/**
 * 커뮤니티 게시글 목록 조회 유스케이스.
 */
public interface ListCommunityPostsUseCase {

    /**
     * 도시/타입 조건으로 게시글을 조회한다.
     *
     * @param query 게시글 조회 조건
     * @return 게시글 목록
     */
    List<CommunityPostView> list(ListCommunityPostsQuery query);

    /**
     * 게시글 조회 조건.
     *
     * @param cityCode 도시 코드
     * @param type 게시글 타입(ALL 조회 시 null)
     */
    record ListCommunityPostsQuery(String cityCode, PostType type) {
    }
}
