package lab.coder.colly.domain.community.application.port.out;

import lab.coder.colly.domain.community.domain.model.CommunityReport;

/**
 * 커뮤니티 신고 저장/집계 아웃바운드 포트.
 */
public interface CommunityReportPort {

    /**
     * 커뮤니티 신고 정보를 저장한다.
     *
     * @param report 저장할 커뮤니티 신고 정보
     * @return 저장된 커뮤니티 신고 정보
     */
    CommunityReport save(CommunityReport report);

    /**
     * 피신고 사용자 기준 신고 누적 건수를 조회한다.
     *
     * @param targetUserId 피신고 사용자 식별자
     * @return 신고 누적 건수
     */
    long countByTargetUserId(Long targetUserId);
}
