package lab.coder.colly.domain.community.adapter.out.persistence.repository;

import lab.coder.colly.domain.community.adapter.out.persistence.entity.CommunityReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityReportJpaRepository extends JpaRepository<CommunityReportEntity, Long> {

    long countByTargetUserId(Long targetUserId);
}
