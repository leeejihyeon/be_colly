package lab.coder.colly.domain.community.application.port.in;

import java.time.LocalDateTime;
import lab.coder.colly.domain.community.domain.model.JoinPolicy;
import lab.coder.colly.domain.community.domain.model.PostType;

/**
 * 커뮤니티 게시글 응답 뷰.
 *
 * @param id 게시글 식별자
 * @param authorUserId 작성자 사용자 식별자
 * @param countryCode 국가 코드
 * @param cityCode 도시 코드
 * @param type 게시글 타입
 * @param content 게시글 본문
 * @param imageUrl 이미지 URL
 * @param locationName 자유형글 위치명
 * @param destination 모임 목적지
 * @param meetingPlace 모임 장소
 * @param meetingAt 모임 시각
 * @param maxParticipants 최대 참여 인원
 * @param joinPolicy 모임 참여 정책
 */
public record CommunityPostView(
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
}
