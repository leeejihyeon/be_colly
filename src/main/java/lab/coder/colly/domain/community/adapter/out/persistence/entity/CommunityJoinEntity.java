package lab.coder.colly.domain.community.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lab.coder.colly.domain.community.domain.model.JoinStatus;
import lab.coder.colly.shared.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "community_join")
public class CommunityJoinEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JoinStatus status;

    /**
     * 모임 참여 요청/상태 엔티티를 생성한다.
     *
     * @param id     엔티티 식별자
     * @param postId 모임글 ID
     * @param userId 참여 요청 사용자 ID
     * @param status 참여 상태
     */
    public CommunityJoinEntity(
            Long id,
            Long postId,
            Long userId,
            JoinStatus status
    ) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.status = status;
    }
}
