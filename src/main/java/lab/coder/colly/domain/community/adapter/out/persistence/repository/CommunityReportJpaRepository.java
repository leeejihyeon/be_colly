package lab.coder.colly.domain.community.adapter.out.persistence.repository;

import lab.coder.colly.domain.community.adapter.out.persistence.entity.CommunityReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 커뮤니티 신고 엔티티 조회용 JPA 리포지토리.
 */
public interface CommunityReportJpaRepository extends JpaRepository<CommunityReportEntity, Long> {

    /**
     * 피신고자 사용자 ID 기준 신고 누적 건수를 조회한다.
     *
     * @param targetUserId 피신고자 사용자 식별자
     * @return 신고 누적 건수
     */
    long countByTargetUserId(Long targetUserId);
}
