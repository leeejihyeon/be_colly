package lab.coder.colly.domain.community.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lab.coder.colly.domain.community.domain.model.RestrictionType;
import lab.coder.colly.shared.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_restriction")
public class UserRestrictionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RestrictionType type;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false, length = 200)
    private String reason;

    /**
     * 사용자 제재 이력 엔티티를 생성한다.
     *
     * @param id 엔티티 식별자
     * @param userId 제재 대상 사용자 ID
     * @param type 제재 유형
     * @param startAt 제재 시작 시각
     * @param endAt 제재 종료 시각
     * @param reason 제재 사유
     */
    public UserRestrictionEntity(
            Long id,
            Long userId,
            RestrictionType type,
            LocalDateTime startAt,
            LocalDateTime endAt,
            String reason
    ) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.startAt = startAt;
        this.endAt = endAt;
        this.reason = reason;
    }
}
