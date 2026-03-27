package lab.coder.colly.domain.community.application.port.in;

/**
 * 사용자 신고 유스케이스.
 */
public interface ReportUserUseCase {

    /**
     * 사용자 신고를 접수하고 누적 신고 결과를 반환한다.
     *
     * @param command 신고 요청 정보
     * @return 신고 처리 결과
     */
    ReportResult report(ReportCommand command);

    /**
     * 신고 요청 명령.
     *
     * @param reporterUserId 신고자 사용자 ID
     * @param targetUserId 피신고자 사용자 ID
     * @param reason 신고 사유
     */
    record ReportCommand(Long reporterUserId, Long targetUserId, String reason) {
    }

    /**
     * 신고 처리 결과.
     *
     * @param targetUserId 피신고자 사용자 ID
     * @param reportCount 누적 신고 건수
     * @param restricted 제재 적용 여부
     */
    record ReportResult(Long targetUserId, long reportCount, boolean restricted) {
    }
}
