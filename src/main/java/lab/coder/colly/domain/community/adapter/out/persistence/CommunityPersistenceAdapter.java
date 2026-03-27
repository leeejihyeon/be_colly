package lab.coder.colly.domain.community.adapter.out.persistence;

import lab.coder.colly.domain.community.adapter.out.persistence.entity.CommunityJoinEntity;
import lab.coder.colly.domain.community.adapter.out.persistence.entity.CommunityPostEntity;
import lab.coder.colly.domain.community.adapter.out.persistence.entity.CommunityReportEntity;
import lab.coder.colly.domain.community.adapter.out.persistence.entity.UserRestrictionEntity;
import lab.coder.colly.domain.community.adapter.out.persistence.repository.CommunityJoinJpaRepository;
import lab.coder.colly.domain.community.adapter.out.persistence.repository.CommunityPostJpaRepository;
import lab.coder.colly.domain.community.adapter.out.persistence.repository.CommunityReportJpaRepository;
import lab.coder.colly.domain.community.adapter.out.persistence.repository.UserRestrictionJpaRepository;
import lab.coder.colly.domain.community.application.port.out.CommunityJoinPort;
import lab.coder.colly.domain.community.application.port.out.CommunityPostPort;
import lab.coder.colly.domain.community.application.port.out.CommunityReportPort;
import lab.coder.colly.domain.community.application.port.out.UserRestrictionPort;
import lab.coder.colly.domain.community.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommunityPersistenceAdapter implements CommunityPostPort, CommunityJoinPort, CommunityReportPort, UserRestrictionPort {

    private final CommunityPostJpaRepository communityPostJpaRepository;
    private final CommunityJoinJpaRepository communityJoinJpaRepository;
    private final CommunityReportJpaRepository communityReportJpaRepository;
    private final UserRestrictionJpaRepository userRestrictionJpaRepository;

    /**
     * 커뮤니티 게시글을 저장한다.
     *
     * @param post 저장할 게시글 도메인 모델
     * @return 저장된 게시글 도메인 모델
     */
    @Override
    public CommunityPost save(CommunityPost post) {
        CommunityPostEntity saved =
                communityPostJpaRepository.save(
                        new CommunityPostEntity(
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
                        )
                );
        return toDomain(saved);
    }

    /**
     * 게시글 식별자로 게시글을 조회한다.
     *
     * @param postId 게시글 식별자
     * @return 게시글 조회 결과
     */
    @Override
    public Optional<CommunityPost> findPostById(Long postId) {
        return communityPostJpaRepository.findById(postId)
                .map(this::toDomain);
    }

    /**
     * 국가/도시/타입 조건으로 게시글 목록을 조회한다.
     *
     * @param countryCode 국가 코드
     * @param cityCode 도시 코드
     * @param type     게시글 타입(ALL일 경우 null)
     * @return 게시글 목록
     */
    @Override
    public List<CommunityPost> findByCountryCodeAndCityCodeAndType(
            String countryCode,
            String cityCode,
            PostType type
    ) {
        List<CommunityPostEntity> entities = (type == null)
                ? communityPostJpaRepository.findByCountryCodeAndCityCodeOrderByIdDesc(countryCode, cityCode)
                : communityPostJpaRepository.findByCountryCodeAndCityCodeAndTypeOrderByIdDesc(countryCode, cityCode, type);

        return entities.stream()
                .map(this::toDomain)
                .toList();
    }

    /**
     * 모임 참여 정보를 저장한다.
     *
     * @param join 저장할 참여 도메인 모델
     * @return 저장된 참여 도메인 모델
     */
    @Override
    public CommunityJoin save(CommunityJoin join) {
        CommunityJoinEntity saved =
                communityJoinJpaRepository.save(
                        new CommunityJoinEntity(
                                join.getId(),
                                join.getPostId(),
                                join.getUserId(),
                                join.getStatus()
                        )
                );

        return toDomain(saved);
    }

    /**
     * 참여 식별자로 참여 정보를 조회한다.
     *
     * @param joinId 참여 식별자
     * @return 참여 정보 조회 결과
     */
    @Override
    public Optional<CommunityJoin> findJoinById(Long joinId) {
        return communityJoinJpaRepository.findById(joinId)
                .map(this::toDomain);
    }

    /**
     * 게시글/사용자 기준 참여 정보를 조회한다.
     *
     * @param postId 게시글 식별자
     * @param userId 사용자 식별자
     * @return 참여 정보 조회 결과
     */
    @Override
    public Optional<CommunityJoin> findByPostIdAndUserId(
            Long postId,
            Long userId
    ) {
        return communityJoinJpaRepository.findByPostIdAndUserId(postId, userId)
                .map(this::toDomain);
    }

    /**
     * 특정 게시글의 승인 완료 참여 인원 수를 조회한다.
     *
     * @param postId 게시글 식별자
     * @return 승인 상태 참여 인원 수
     */
    @Override
    public long countApprovedByPostId(Long postId) {
        return communityJoinJpaRepository.countByPostIdAndStatus(
                postId,
                JoinStatus.APPROVED
        );
    }

    /**
     * 신고 정보를 저장한다.
     *
     * @param report 저장할 신고 도메인 모델
     * @return 저장된 신고 도메인 모델
     */
    @Override
    public CommunityReport save(CommunityReport report) {
        CommunityReportEntity saved =
                communityReportJpaRepository.save(
                        new CommunityReportEntity(
                                report.getId(),
                                report.getReporterUserId(),
                                report.getTargetUserId(),
                                report.getReason()
                        )
                );

        return CommunityReport.restore(
                saved.getId(),
                saved.getReporterUserId(),
                saved.getTargetUserId(),
                saved.getReason()
        );
    }

    /**
     * 사용자별 신고 누적 건수를 조회한다.
     *
     * @param targetUserId 피신고자 사용자 식별자
     * @return 신고 건수
     */
    @Override
    public long countByTargetUserId(Long targetUserId) {
        return communityReportJpaRepository.countByTargetUserId(targetUserId);
    }

    /**
     * 사용자 제재 정보를 저장한다.
     *
     * @param restriction 저장할 제재 도메인 모델
     * @return 저장된 제재 도메인 모델
     */
    @Override
    public UserRestriction save(UserRestriction restriction) {
        UserRestrictionEntity saved =
                userRestrictionJpaRepository.save(
                        new UserRestrictionEntity(
                                restriction.getId(),
                                restriction.getUserId(),
                                restriction.getType(),
                                restriction.getStartAt(),
                                restriction.getEndAt(),
                                restriction.getReason()
                        )
                );

        return toDomain(saved);
    }

    /**
     * 현재 시점 활성 제재를 조회한다.
     *
     * @param userId 사용자 식별자
     * @param now    기준 시각
     * @return 활성 제재 조회 결과
     */
    @Override
    public Optional<UserRestriction> findActiveByUserId(
            Long userId,
            LocalDateTime now
    ) {
        return userRestrictionJpaRepository
                .findFirstByUserIdAndStartAtLessThanEqualAndEndAtGreaterThanEqualOrderByIdDesc(userId, now, now)
                .map(this::toDomain);
    }

    /**
     * 게시글 JPA 엔티티를 도메인 모델로 변환한다.
     *
     * @param entity 게시글 JPA 엔티티
     * @return 게시글 도메인 모델
     */
    private CommunityPost toDomain(CommunityPostEntity entity) {
        return CommunityPost.restore(
                entity.getId(),
                entity.getAuthorUserId(),
                entity.getCountryCode(),
                entity.getCityCode(),
                entity.getType(),
                entity.getContent(),
                entity.getImageUrl(),
                entity.getLocationName(),
                entity.getDestination(),
                entity.getMeetingPlace(),
                entity.getMeetingAt(),
                entity.getMaxParticipants(),
                entity.getJoinPolicy()
        );
    }

    /**
     * 참여 JPA 엔티티를 도메인 모델로 변환한다.
     *
     * @param entity 참여 JPA 엔티티
     * @return 참여 도메인 모델
     */
    private CommunityJoin toDomain(CommunityJoinEntity entity) {
        return CommunityJoin.restore(
                entity.getId(),
                entity.getPostId(),
                entity.getUserId(),
                entity.getStatus()
        );
    }

    /**
     * 제재 JPA 엔티티를 도메인 모델로 변환한다.
     *
     * @param entity 제재 JPA 엔티티
     * @return 제재 도메인 모델
     */
    private UserRestriction toDomain(UserRestrictionEntity entity) {
        return UserRestriction.restore(
                entity.getId(),
                entity.getUserId(),
                entity.getType(),
                entity.getStartAt(),
                entity.getEndAt(),
                entity.getReason()
        );
    }
}
