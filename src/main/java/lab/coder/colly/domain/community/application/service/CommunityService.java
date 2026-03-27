package lab.coder.colly.domain.community.application.service;

import lab.coder.colly.domain.community.application.port.in.*;
import lab.coder.colly.domain.community.application.port.out.CommunityJoinPort;
import lab.coder.colly.domain.community.application.port.out.CommunityPostPort;
import lab.coder.colly.domain.community.application.port.out.CommunityReportPort;
import lab.coder.colly.domain.community.application.port.out.UserRestrictionPort;
import lab.coder.colly.domain.community.domain.model.*;
import lab.coder.colly.domain.community.domain.policy.CommunityPostPolicy;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 커뮤니티 유스케이스 구현 서비스.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommunityService implements
        CreateCommunityPostUseCase,
        ListCommunityPostsUseCase,
        JoinCommunityUseCase,
        ReviewCommunityJoinUseCase,
        ReportUserUseCase,
        GetRestrictionUseCase {

    private static final long REPORT_THRESHOLD = 3L;

    private final CommunityPostPort communityPostPort;
    private final CommunityJoinPort communityJoinPort;
    private final CommunityReportPort communityReportPort;
    private final UserRestrictionPort userRestrictionPort;

    /**
     * 커뮤니티 게시글을 생성한다.
     *
     * @param command 게시글 생성 명령
     * @return 생성된 게시글 뷰
     */
    @Override
    @Transactional
    public CommunityPostView createPost(CreateCommunityPostCommand command) {

        ensureNotRestricted(command.authorUserId());
        CommunityPostPolicy.validateByType(command);

        CommunityPost saved =
                communityPostPort.save(
                        CommunityPost.create(
                                command.authorUserId(),
                                command.countryCode(),
                                command.cityCode(),
                                command.type(),
                                command.content(),
                                command.imageUrl(),
                                command.locationName(),
                                command.destination(),
                                command.meetingPlace(),
                                command.meetingAt(),
                                command.maxParticipants(),
                                command.joinPolicy()
                        )
                );

        return toPostView(saved);
    }

    /**
     * 국가/도시/타입 조건으로 게시글 피드를 조회한다.
     *
     * @param query 게시글 조회 조건
     * @return 게시글 목록
     */
    @Override
    public List<CommunityPostView> list(ListCommunityPostsQuery query) {

        PostType type = query.type();
        List<CommunityPost> posts =
                communityPostPort.findByCountryCodeAndCityCodeAndType(
                        query.countryCode(),
                        query.cityCode(),
                        type
                );

        return posts.stream()
                .map(this::toPostView)
                .toList();
    }

    /**
     * 모임글 참여를 처리한다.
     *
     * @param command 참여 요청 정보
     * @return 참여 결과 뷰
     */
    @Override
    @Transactional
    public CommunityJoinView join(JoinCommunityCommand command) {

        ensureNotRestricted(command.userId());

        CommunityPost post = communityPostPort.findPostById(command.postId())
                .orElseThrow(() ->
                        new DomainException(
                                ErrorCode.POST_NOT_FOUND,
                                "Post not found: " + command.postId())
                );

        if (post.getType() != PostType.GATHERING) {
            throw new DomainException(
                    ErrorCode.JOIN_NOT_ALLOWED,
                    "Only gathering posts can be joined"
            );
        }

        communityJoinPort.findByPostIdAndUserId(command.postId(), command.userId())
                .ifPresent(existing -> {
                    if (existing.getStatus() != JoinStatus.CANCELED) {
                        throw new DomainException(
                                ErrorCode.JOIN_ALREADY_EXISTS,
                                "Join already exists"
                        );
                    }
                });

        JoinStatus initStatus = post.getJoinPolicy() == JoinPolicy.APPROVAL
                ? JoinStatus.PENDING
                : JoinStatus.APPROVED;

        if (initStatus == JoinStatus.APPROVED && post.getMaxParticipants() != null) {
            long approvedCount = communityJoinPort.countApprovedByPostId(post.getId());
            if (approvedCount >= post.getMaxParticipants()) {
                throw new DomainException(
                        ErrorCode.JOIN_NOT_ALLOWED,
                        "Gathering is full"
                );
            }
        }

        CommunityJoin saved = communityJoinPort.save(
                CommunityJoin.create(
                        post.getId(),
                        command.userId(),
                        initStatus
                )
        );

        return toJoinView(saved);
    }

    /**
     * 모임 참여 신청을 승인/거절한다.
     *
     * @param command 참여 검토 요청 정보
     * @return 변경된 참여 상태 뷰
     */
    @Override
    @Transactional
    public CommunityJoinView review(ReviewCommunityJoinCommand command) {
        ensureNotRestricted(command.hostUserId());

        CommunityJoin join = communityJoinPort.findJoinById(command.joinId())
                .orElseThrow(() ->
                        new DomainException(
                                ErrorCode.JOIN_NOT_FOUND,
                                "Join not found: " + command.joinId()
                        )
                );

        CommunityPost post = communityPostPort.findPostById(join.getPostId())
                .orElseThrow(() ->
                        new DomainException(
                                ErrorCode.POST_NOT_FOUND,
                                "Post not found: " + join.getPostId()
                        )
                );

        if (!post.getAuthorUserId().equals(command.hostUserId())) {
            throw new DomainException(
                    ErrorCode.FORBIDDEN_ACTION,
                    "Only host can review joins"
            );
        }

        if (post.getType() != PostType.GATHERING || post.getJoinPolicy() != JoinPolicy.APPROVAL) {
            throw new DomainException(
                    ErrorCode.JOIN_NOT_ALLOWED,
                    "Review is allowed only for approval gathering"
            );
        }

        if (command.status() != JoinStatus.APPROVED && command.status() != JoinStatus.REJECTED) {
            throw new DomainException(
                    ErrorCode.JOIN_NOT_ALLOWED,
                    "Review status must be APPROVED or REJECTED"
            );
        }

        if (command.status() == JoinStatus.APPROVED && post.getMaxParticipants() != null) {
            long approvedCount = communityJoinPort.countApprovedByPostId(post.getId());
            if (approvedCount >= post.getMaxParticipants()) {
                throw new DomainException(
                        ErrorCode.JOIN_NOT_ALLOWED,
                        "Gathering is full"
                );
            }
        }

        CommunityJoin updated = communityJoinPort.save(join.changeStatus(command.status()));

        return toJoinView(updated);
    }

    /**
     * 사용자를 신고하고 누적 신고 건수 기준으로 제재를 적용한다.
     *
     * @param command 신고 요청 정보
     * @return 신고 처리 결과
     */
    @Override
    @Transactional
    public ReportResult report(ReportCommand command) {

        communityReportPort.save(
                CommunityReport.create(
                        command.reporterUserId(),
                        command.targetUserId(),
                        command.reason()
                )
        );

        long count = communityReportPort.countByTargetUserId(command.targetUserId());

        boolean restricted = false;
        if (count >= REPORT_THRESHOLD) {
            LocalDateTime now = LocalDateTime.now();
            boolean active = userRestrictionPort.findActiveByUserId(command.targetUserId(), now).isPresent();
            if (!active) {
                userRestrictionPort.save(
                        UserRestriction.create(
                                command.targetUserId(),
                                RestrictionType.COMMUNITY_ACTIVITY_BAN,
                                now,
                                now.plusDays(7),
                                "3 reports accumulated"
                        )
                );
            }
            restricted = true;
        }

        return new ReportResult(
                command.targetUserId(),
                count,
                restricted
        );
    }

    /**
     * 사용자의 활성 제재 상태를 조회한다.
     *
     * @param userId 사용자 식별자
     * @return 제재 상태 뷰
     */
    @Override
    public RestrictionView getActiveRestriction(Long userId) {
        return userRestrictionPort.findActiveByUserId(userId, LocalDateTime.now())
                .map(r ->
                        new RestrictionView(
                                true,
                                r.getStartAt(),
                                r.getEndAt(),
                                r.getReason()
                        )
                )
                .orElse(
                        new RestrictionView(
                                false,
                                null,
                                null,
                                null
                        )
                );
    }

    /**
     * 현재 시점 기준으로 사용자의 활동 제한 여부를 검증한다.
     *
     * @param userId 사용자 식별자
     */
    private void ensureNotRestricted(Long userId) {
        userRestrictionPort.findActiveByUserId(userId, LocalDateTime.now())
                .ifPresent(r -> {
                    throw new DomainException(
                            ErrorCode.USER_RESTRICTED,
                            "User is restricted until " + r.getEndAt()
                    );
                });
    }

    /**
     * 도메인 게시글 모델을 API 응답 뷰로 변환한다.
     *
     * @param post 도메인 게시글
     * @return 게시글 응답 뷰
     */
    private CommunityPostView toPostView(CommunityPost post) {
        return new CommunityPostView(
                post.getId(),
                post.getAuthorUserId(),
                post.getCountryCode(),
                post.getCityCode(),
                post.getType(),
                post.getContent(),
                post.getImageUrl(),
                post.getLocationName(),
                post.getDestination(),
                post.getMeetingPlace(),
                post.getMeetingAt(),
                post.getMaxParticipants(),
                post.getJoinPolicy()
        );
    }

    /**
     * 도메인 참여 모델을 API 응답 뷰로 변환한다.
     *
     * @param join 도메인 참여 정보
     * @return 참여 응답 뷰
     */
    private CommunityJoinView toJoinView(CommunityJoin join) {
        return new CommunityJoinView(
                join.getId(),
                join.getPostId(),
                join.getUserId(),
                join.getStatus()
        );
    }
}
