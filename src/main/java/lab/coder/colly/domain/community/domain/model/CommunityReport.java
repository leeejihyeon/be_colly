package lab.coder.colly.domain.community.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 커뮤니티 신고 도메인 모델이다.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommunityReport {

    private final Long id;
    private final Long reporterUserId;
    private final Long targetUserId;
    private final String reason;

    /**
     * 신규 신고 도메인 객체를 생성한다.
     *
     * @param reporterUserId 신고자 사용자 ID
     * @param targetUserId   피신고자 사용자 ID
     * @param reason         신고 사유
     * @return 신규 신고 도메인 객체
     */
    public static CommunityReport create(
            Long reporterUserId,
            Long targetUserId,
            String reason
    ) {
        return new CommunityReport(
                null,
                reporterUserId,
                targetUserId,
                reason
        );
    }

    /**
     * 저장소 데이터로 신고 도메인 객체를 복원한다.
     *
     * @param id             신고 ID
     * @param reporterUserId 신고자 사용자 ID
     * @param targetUserId   피신고자 사용자 ID
     * @param reason         신고 사유
     * @return 복원된 신고 도메인 객체
     */
    public static CommunityReport restore(
            Long id,
            Long reporterUserId,
            Long targetUserId,
            String reason
    ) {
        return new CommunityReport(
                id,
                reporterUserId,
                targetUserId,
                reason
        );
    }
}
