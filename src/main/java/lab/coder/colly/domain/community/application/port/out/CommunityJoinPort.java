package lab.coder.colly.domain.community.application.port.out;

import java.util.Optional;
import lab.coder.colly.domain.community.domain.model.CommunityJoin;

/**
 * 커뮤니티 모임 참여 저장/조회 아웃바운드 포트.
 */
public interface CommunityJoinPort {

    /**
     * 모임 참여 정보를 저장한다.
     *
     * @param join 저장할 모임 참여 정보
     * @return 저장된 모임 참여 정보
     */
    CommunityJoin save(CommunityJoin join);

    /**
     * 모임 참여 식별자로 참여 정보를 조회한다.
     *
     * @param joinId 모임 참여 식별자
     * @return 모임 참여 조회 결과
     */
    Optional<CommunityJoin> findJoinById(Long joinId);

    /**
     * 게시글과 사용자 기준으로 모임 참여 정보를 조회한다.
     *
     * @param postId 게시글 식별자
     * @param userId 사용자 식별자
     * @return 모임 참여 조회 결과
     */
    Optional<CommunityJoin> findByPostIdAndUserId(Long postId, Long userId);

    /**
     * 게시글의 승인 상태 참여 인원 수를 조회한다.
     *
     * @param postId 게시글 식별자
     * @return 승인 상태 참여 인원 수
     */
    long countApprovedByPostId(Long postId);
}
