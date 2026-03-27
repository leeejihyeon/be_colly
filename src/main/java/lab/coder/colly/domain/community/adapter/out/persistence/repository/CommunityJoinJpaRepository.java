package lab.coder.colly.domain.community.adapter.out.persistence.repository;

import java.util.Optional;
import lab.coder.colly.domain.community.adapter.out.persistence.entity.CommunityJoinEntity;
import lab.coder.colly.domain.community.domain.model.JoinStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityJoinJpaRepository extends JpaRepository<CommunityJoinEntity, Long> {

    Optional<CommunityJoinEntity> findByPostIdAndUserId(Long postId, Long userId);

    long countByPostIdAndStatus(Long postId, JoinStatus status);
}
