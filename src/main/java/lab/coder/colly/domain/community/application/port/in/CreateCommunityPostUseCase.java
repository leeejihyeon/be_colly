package lab.coder.colly.domain.community.application.port.in;

import java.time.LocalDateTime;
import lab.coder.colly.domain.community.domain.model.JoinPolicy;
import lab.coder.colly.domain.community.domain.model.PostType;

/**
 * 커뮤니티 게시글 생성 유스케이스.
 */
public interface CreateCommunityPostUseCase {

    /**
     * 커뮤니티 게시글을 생성한다.
     *
     * @param command 게시글 생성 요청 정보
     * @return 생성된 게시글 뷰
     */
    CommunityPostView createPost(CreateCommunityPostCommand command);

    /**
     * 게시글 생성 명령.
     *
     * @param authorUserId 작성자 사용자 ID
     * @param cityCode 도시 코드
     * @param type 게시글 타입
     * @param content 게시글 본문
     * @param imageUrl 이미지 URL
     * @param locationName 위치 텍스트
     * @param destination 모임 목적지
     * @param meetingPlace 모임 장소
     * @param meetingAt 모임 시간
     * @param maxParticipants 최대 참여 인원
     * @param joinPolicy 모임 참여 정책
     */
    record CreateCommunityPostCommand(
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
    }
}
