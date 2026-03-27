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
import lab.coder.colly.domain.community.domain.model.JoinPolicy;
import lab.coder.colly.domain.community.domain.model.PostType;
import lab.coder.colly.shared.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "community_post")
public class CommunityPostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long authorUserId;

    @Column(nullable = false, length = 50)
    private String cityCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostType type;

    @Column(nullable = false, length = 4000)
    private String content;

    @Column(length = 1000)
    private String imageUrl;

    @Column(length = 200)
    private String locationName;

    @Column(length = 200)
    private String destination;

    @Column(length = 200)
    private String meetingPlace;

    private LocalDateTime meetingAt;

    private Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private JoinPolicy joinPolicy;

    /**
     * 커뮤니티 게시글 엔티티를 생성한다.
     *
     * @param id 엔티티 식별자
     * @param authorUserId 작성자 사용자 ID
     * @param cityCode 게시글 대상 도시 코드
     * @param type 게시글 타입
     * @param content 본문
     * @param imageUrl 이미지 URL
     * @param locationName 자유형글 위치명
     * @param destination 모임 목적지
     * @param meetingPlace 모임 만남 장소
     * @param meetingAt 모임 시각
     * @param maxParticipants 최대 참여 인원
     * @param joinPolicy 참여 정책
     */
    public CommunityPostEntity(
            Long id,
            Long authorUserId,
            String cityCode,
            PostType type,
            String content,
            String imageUrl,
            String locationName,
            String destination,
            String meetingPlace,
            LocalDateTime meetingAt,
            Integer maxParticipants,
            JoinPolicy joinPolicy
    ) {
        this.id = id;
        this.authorUserId = authorUserId;
        this.cityCode = cityCode;
        this.type = type;
        this.content = content;
        this.imageUrl = imageUrl;
        this.locationName = locationName;
        this.destination = destination;
        this.meetingPlace = meetingPlace;
        this.meetingAt = meetingAt;
        this.maxParticipants = maxParticipants;
        this.joinPolicy = joinPolicy;
    }
}
