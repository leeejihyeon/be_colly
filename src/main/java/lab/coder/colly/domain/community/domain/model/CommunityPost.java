package lab.coder.colly.domain.community.domain.model;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 커뮤니티 게시글 도메인 모델이다.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommunityPost {

    private final Long id;
    private final Long authorUserId;
    private final String countryCode;
    private final String cityCode;
    private final PostType type;
    private final String content;
    private final String imageUrl;
    private final String locationName;
    private final String destination;
    private final String meetingPlace;
    private final LocalDateTime meetingAt;
    private final Integer maxParticipants;
    private final JoinPolicy joinPolicy;

    /**
     * 신규 커뮤니티 게시글 도메인 객체를 생성한다.
     *
     * @param authorUserId 작성자 사용자 ID
     * @param countryCode 국가 코드
     * @param cityCode 도시 코드
     * @param type 게시글 타입
     * @param content 게시글 본문
     * @param imageUrl 게시글 이미지 URL
     * @param locationName 위치명
     * @param destination 모임 목적지
     * @param meetingPlace 모임 장소
     * @param meetingAt 모임 일시
     * @param maxParticipants 최대 참여 인원
     * @param joinPolicy 모임 참여 정책
     * @return 신규 게시글 도메인 객체
     */
    public static CommunityPost create(
        Long authorUserId,
        String countryCode,
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
        return new CommunityPost(
            null,
            authorUserId,
            countryCode,
            cityCode,
            type,
            content,
            imageUrl,
            locationName,
            destination,
            meetingPlace,
            meetingAt,
            maxParticipants,
            joinPolicy
        );
    }

    /**
     * 저장소에서 조회한 데이터로 커뮤니티 게시글 도메인 객체를 복원한다.
     *
     * @param id 게시글 ID
     * @param authorUserId 작성자 사용자 ID
     * @param countryCode 국가 코드
     * @param cityCode 도시 코드
     * @param type 게시글 타입
     * @param content 게시글 본문
     * @param imageUrl 게시글 이미지 URL
     * @param locationName 위치명
     * @param destination 모임 목적지
     * @param meetingPlace 모임 장소
     * @param meetingAt 모임 일시
     * @param maxParticipants 최대 참여 인원
     * @param joinPolicy 모임 참여 정책
     * @return 복원된 게시글 도메인 객체
     */
    public static CommunityPost restore(
        Long id,
        Long authorUserId,
        String countryCode,
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
        return new CommunityPost(
            id,
            authorUserId,
            countryCode,
            cityCode,
            type,
            content,
            imageUrl,
            locationName,
            destination,
            meetingPlace,
            meetingAt,
            maxParticipants,
            joinPolicy
        );
    }
}
