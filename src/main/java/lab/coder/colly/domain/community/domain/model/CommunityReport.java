package lab.coder.colly.domain.community.domain.model;

public class CommunityReport {

    private final Long id;
    private final Long reporterUserId;
    private final Long targetUserId;
    private final String reason;

    private CommunityReport(Long id, Long reporterUserId, Long targetUserId, String reason) {
        this.id = id;
        this.reporterUserId = reporterUserId;
        this.targetUserId = targetUserId;
        this.reason = reason;
    }

    public static CommunityReport create(Long reporterUserId, Long targetUserId, String reason) {
        return new CommunityReport(null, reporterUserId, targetUserId, reason);
    }

    public static CommunityReport restore(Long id, Long reporterUserId, Long targetUserId, String reason) {
        return new CommunityReport(id, reporterUserId, targetUserId, reason);
    }

    public Long getId() {
        return id;
    }

    public Long getReporterUserId() {
        return reporterUserId;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public String getReason() {
        return reason;
    }
}
