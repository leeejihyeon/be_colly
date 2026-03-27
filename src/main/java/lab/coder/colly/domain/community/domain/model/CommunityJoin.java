package lab.coder.colly.domain.community.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 커뮤니티 모임 참여 상태 도메인 모델이다.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommunityJoin {

    private final Long id;
    private final Long postId;
    private final Long userId;
    private final JoinStatus status;

    /**
     * 신규 모임 참여 도메인 객체를 생성한다.
     *
     * @param postId 게시글 ID
     * @param userId 참여 사용자 ID
     * @param status 참여 상태
     * @return 신규 모임 참여 도메인 객체
     */
    public static CommunityJoin create(
            Long postId,
            Long userId,
            JoinStatus status
    ) {
        return new CommunityJoin(
                null,
                postId,
                userId,
                status
        );
    }

    /**
     * 저장소 데이터로 모임 참여 도메인 객체를 복원한다.
     *
     * @param id     모임 참여 ID
     * @param postId 게시글 ID
     * @param userId 참여 사용자 ID
     * @param status 참여 상태
     * @return 복원된 모임 참여 도메인 객체
     */
    public static CommunityJoin restore(
            Long id,
            Long postId,
            Long userId,
            JoinStatus status
    ) {
        return new CommunityJoin(
                id,
                postId,
                userId,
                status
        );
    }

    /**
     * 모임 참여 상태를 변경한 새 도메인 객체를 반환한다.
     *
     * @param nextStatus 변경할 다음 상태
     * @return 상태가 변경된 모임 참여 도메인 객체
     */
    public CommunityJoin changeStatus(JoinStatus nextStatus) {
        return new CommunityJoin(
                id,
                postId,
                userId,
                nextStatus
        );
    }
}
