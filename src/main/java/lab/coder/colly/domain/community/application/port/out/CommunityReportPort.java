package lab.coder.colly.domain.community.application.port.out;

import lab.coder.colly.domain.community.domain.model.CommunityReport;

public interface CommunityReportPort {

    CommunityReport save(CommunityReport report);

    long countByTargetUserId(Long targetUserId);
}
