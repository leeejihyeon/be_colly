package lab.coder.colly.domain.community.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lab.coder.colly.domain.community.application.port.in.CreateCommunityPostUseCase;
import lab.coder.colly.domain.community.application.port.in.JoinCommunityUseCase;
import lab.coder.colly.domain.community.application.port.in.ListCommunityPostsUseCase;
import lab.coder.colly.domain.community.application.port.in.ReportUserUseCase;
import lab.coder.colly.domain.community.application.port.out.CommunityJoinPort;
import lab.coder.colly.domain.community.application.port.out.CommunityPostPort;
import lab.coder.colly.domain.community.application.port.out.CommunityReportPort;
import lab.coder.colly.domain.community.application.port.out.UserRestrictionPort;
import lab.coder.colly.domain.community.domain.model.CommunityJoin;
import lab.coder.colly.domain.community.domain.model.CommunityPost;
import lab.coder.colly.domain.community.domain.model.JoinPolicy;
import lab.coder.colly.domain.community.domain.model.JoinStatus;
import lab.coder.colly.domain.community.domain.model.PostType;
import lab.coder.colly.shared.error.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommunityServiceTest {

    @Mock
    private CommunityPostPort communityPostPort;

    @Mock
    private CommunityJoinPort communityJoinPort;

    @Mock
    private CommunityReportPort communityReportPort;

    @Mock
    private UserRestrictionPort userRestrictionPort;

    @InjectMocks
    private CommunityService communityService;

    @Test
    void createPost_rejectsGatheringWhenRequiredFieldsMissing() {
        var command = new CreateCommunityPostUseCase.CreateCommunityPostCommand(
            1L, "SYD", PostType.GATHERING, "content", null, null,
            null, null, null, null, null
        );

        assertThatThrownBy(() -> communityService.createPost(command)).isInstanceOf(DomainException.class);
    }

    @Test
    void createPost_rejectsFreeFeedWhenGatheringFieldsProvided() {
        var command = new CreateCommunityPostUseCase.CreateCommunityPostCommand(
            1L, "SYD", PostType.FREE_FEED, "content", null, "Bondi",
            "dest", null, null, null, null
        );

        assertThatThrownBy(() -> communityService.createPost(command)).isInstanceOf(DomainException.class);
    }

    @Test
    void join_rejectsWhenPostTypeIsFreeFeed() {
        when(communityPostPort.findById(10L)).thenReturn(Optional.of(CommunityPost.restore(
            10L, 2L, "SYD", PostType.FREE_FEED, "text", null, "Sydney", null, null, null, null, null
        )));

        assertThatThrownBy(() -> communityService.join(new JoinCommunityUseCase.JoinCommunityCommand(10L, 3L)))
            .isInstanceOf(DomainException.class);
    }

    @Test
    void list_filtersByType() {
        when(communityPostPort.findByCityCodeAndType("SYD", PostType.GATHERING)).thenReturn(List.of(
            CommunityPost.restore(1L, 1L, "SYD", PostType.GATHERING, "a", null, null, "dest", "place", LocalDateTime.now(), 4, JoinPolicy.FREE)
        ));

        var result = communityService.list(new ListCommunityPostsUseCase.ListCommunityPostsQuery("SYD", PostType.GATHERING));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).type()).isEqualTo(PostType.GATHERING);
    }

    @Test
    void report_appliesRestrictionWhenCountReachesThree() {
        when(communityReportPort.countByTargetUserId(9L)).thenReturn(3L);
        when(userRestrictionPort.findActiveByUserId(any(), any())).thenReturn(Optional.empty());

        ReportUserUseCase.ReportResult result = communityService.report(new ReportUserUseCase.ReportCommand(1L, 9L, "abuse"));

        assertThat(result.restricted()).isTrue();
    }

    @Test
    void join_approvalPolicyCreatesPendingStatus() {
        when(communityPostPort.findById(100L)).thenReturn(Optional.of(CommunityPost.restore(
            100L, 2L, "SYD", PostType.GATHERING, "g", null, null,
            "Blue", "Town Hall", LocalDateTime.now().plusDays(1), 10, JoinPolicy.APPROVAL
        )));
        when(communityJoinPort.findByPostIdAndUserId(100L, 7L)).thenReturn(Optional.empty());
        when(communityJoinPort.save(any())).thenReturn(CommunityJoin.restore(1L, 100L, 7L, JoinStatus.PENDING));

        var result = communityService.join(new JoinCommunityUseCase.JoinCommunityCommand(100L, 7L));

        assertThat(result.status()).isEqualTo(JoinStatus.PENDING);
    }
}
