package lab.coder.colly.domain.community.adapter.in.web;

import jakarta.validation.Valid;
import lab.coder.colly.domain.community.adapter.in.web.dto.CreateCommunityPostRequest;
import lab.coder.colly.domain.community.adapter.in.web.dto.JoinRequest;
import lab.coder.colly.domain.community.adapter.in.web.dto.ReportRequest;
import lab.coder.colly.domain.community.adapter.in.web.dto.ReviewJoinRequest;
import lab.coder.colly.domain.community.application.port.in.*;
import lab.coder.colly.domain.community.domain.model.PostType;
import lab.coder.colly.shared.api.ApiResponse;
import lab.coder.colly.shared.api.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CreateCommunityPostUseCase createCommunityPostUseCase;
    private final ListCommunityPostsUseCase listCommunityPostsUseCase;
    private final JoinCommunityUseCase joinCommunityUseCase;
    private final ReviewCommunityJoinUseCase reviewCommunityJoinUseCase;
    private final ReportUserUseCase reportUserUseCase;
    private final GetRestrictionUseCase getRestrictionUseCase;

    /**
     * 커뮤니티 게시글을 생성한다.
     *
     * @param request 게시글 생성 요청 바디
     * @return 생성된 게시글 응답
     */
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<CommunityPostView>> createPost(
            @Valid @RequestBody CreateCommunityPostRequest request
    ) {
        CommunityPostView created =
                createCommunityPostUseCase.createPost(
                        new CreateCommunityPostUseCase.CreateCommunityPostCommand(
                                request.authorUserId(),
                                request.countryCode(),
                                request.cityCode(),
                                request.type(),
                                request.content(),
                                request.imageUrl(),
                                request.locationName(),
                                request.destination(),
                                request.meetingPlace(),
                                request.meetingAt(),
                                request.maxParticipants(),
                                request.joinPolicy()
                        )
                );

        return ApiResponses.created(created);
    }

    /**
     * 도시 코드와 게시글 타입 필터로 피드를 조회한다.
     *
     * @param countryCode 국가 코드
     * @param cityCode    도시 코드
     * @param type        게시글 타입 필터(ALL/GATHERING/FREE_FEED)
     * @return 게시글 목록 응답
     */
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<List<CommunityPostView>>> list(
            @RequestParam String countryCode,
            @RequestParam String cityCode,
            @RequestParam(defaultValue = "ALL") String type
    ) {
        PostType postType =
                "ALL".equalsIgnoreCase(type)
                        ? null
                        : PostType.valueOf(type.toUpperCase());

        List<CommunityPostView> result =
                listCommunityPostsUseCase.list(
                        new ListCommunityPostsUseCase.ListCommunityPostsQuery(countryCode, cityCode, postType)
                );

        return ApiResponses.ok(result);
    }

    /**
     * 모임글에 참여하거나 승인 대기 상태로 신청한다.
     *
     * @param postId  게시글 식별자
     * @param request 참여 요청 바디
     * @return 참여 결과 응답
     */
    @PostMapping("/posts/{postId}/join")
    public ResponseEntity<ApiResponse<CommunityJoinView>> join(
            @PathVariable Long postId,
            @Valid @RequestBody JoinRequest request
    ) {
        CommunityJoinView join =
                joinCommunityUseCase.join(
                        new JoinCommunityUseCase.JoinCommunityCommand(postId, request.userId())
                );

        return ApiResponses.created(join);
    }

    /**
     * 모임 참여 요청을 승인 또는 거절한다.
     *
     * @param joinId  참여 요청 식별자
     * @param request 검토 요청 바디
     * @return 변경된 참여 상태 응답
     */
    @PatchMapping("/joins/{joinId}")
    public ResponseEntity<ApiResponse<CommunityJoinView>> review(
            @PathVariable Long joinId,
            @Valid @RequestBody ReviewJoinRequest request
    ) {
        CommunityJoinView reviewed =
                reviewCommunityJoinUseCase.review(
                        new ReviewCommunityJoinUseCase.ReviewCommunityJoinCommand(
                                joinId,
                                request.hostUserId(),
                                request.status()
                        )
                );

        return ApiResponses.ok(reviewed);
    }

    /**
     * 사용자 신고를 등록한다.
     *
     * @param request 신고 요청 바디
     * @return 신고 처리 결과 응답
     */
    @PostMapping("/reports")
    public ResponseEntity<ApiResponse<ReportUserUseCase.ReportResult>> report(
            @Valid @RequestBody ReportRequest request
    ) {
        ReportUserUseCase.ReportResult result =
                reportUserUseCase.report(
                        new ReportUserUseCase.ReportCommand(
                                request.reporterUserId(),
                                request.targetUserId(),
                                request.reason()
                        )
                );

        return ApiResponses.created(result);
    }

    /**
     * 사용자의 현재 제재 상태를 조회한다.
     *
     * @param userId 사용자 식별자
     * @return 제재 상태 응답
     */
    @GetMapping("/restrictions/{userId}")
    public ResponseEntity<ApiResponse<GetRestrictionUseCase.RestrictionView>> getRestriction(
            @PathVariable Long userId
    ) {
        return ApiResponses.ok(getRestrictionUseCase.getActiveRestriction(userId));
    }
}
