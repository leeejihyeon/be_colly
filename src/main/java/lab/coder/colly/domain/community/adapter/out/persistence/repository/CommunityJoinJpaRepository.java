package lab.coder.colly.domain.community.adapter.out.persistence.repository;

import lab.coder.colly.domain.community.adapter.out.persistence.entity.CommunityJoinEntity;
import lab.coder.colly.domain.community.domain.model.JoinStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 모임 참여 엔티티 조회용 JPA 리포지토리.
 */
public interface CommunityJoinJpaRepository extends JpaRepository<CommunityJoinEntity, Long> {

    /**
     * 게시글 ID와 사용자 ID로 모임 참여 엔티티를 조회한다.
     *
     * @param postId 게시글 식별자
     * @param userId 사용자 식별자
     * @return 모임 참여 엔티티 조회 결과
     */
    Optional<CommunityJoinEntity> findByPostIdAndUserId(
            Long postId,
            Long userId
    );

    /**
     * 게시글의 특정 참여 상태 건수를 조회한다.
     *
     * @param postId 게시글 식별자
     * @param status 참여 상태
     * @return 상태별 참여 건수
     */
    long countByPostIdAndStatus(
            Long postId,
            JoinStatus status
    );
}
