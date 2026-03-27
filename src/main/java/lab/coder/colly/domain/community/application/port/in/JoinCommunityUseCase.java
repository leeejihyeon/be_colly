package lab.coder.colly.domain.community.application.port.in;

/**
 * 커뮤니티 모임 참여 유스케이스.
 */
public interface JoinCommunityUseCase {

    /**
     * 모임글 참여를 처리한다.
     *
     * @param command 모임 참여 요청 정보
     * @return 참여 처리 결과
     */
    CommunityJoinView join(JoinCommunityCommand command);

    /**
     * 모임 참여 명령.
     *
     * @param postId 게시글 식별자
     * @param userId 참여 사용자 식별자
     */
    record JoinCommunityCommand(Long postId, Long userId) {
    }
}
