package lab.coder.colly.domain.community.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lab.coder.colly.shared.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "community_report")
public class CommunityReportEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reporterUserId;

    @Column(nullable = false)
    private Long targetUserId;

    @Column(nullable = false, length = 500)
    private String reason;

    /**
     * 커뮤니티 신고 엔티티를 생성한다.
     *
     * @param id             엔티티 식별자
     * @param reporterUserId 신고자 사용자 ID
     * @param targetUserId   신고 대상 사용자 ID
     * @param reason         신고 사유
     */
    public CommunityReportEntity(
            Long id,
            Long reporterUserId,
            Long targetUserId,
            String reason
    ) {
        this.id = id;
        this.reporterUserId = reporterUserId;
        this.targetUserId = targetUserId;
        this.reason = reason;
    }
}
