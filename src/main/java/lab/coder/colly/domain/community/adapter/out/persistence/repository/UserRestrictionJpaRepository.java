package lab.coder.colly.domain.community.adapter.out.persistence.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import lab.coder.colly.domain.community.adapter.out.persistence.entity.UserRestrictionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 사용자 제재 엔티티 조회용 JPA 리포지토리.
 */
public interface UserRestrictionJpaRepository extends JpaRepository<UserRestrictionEntity, Long> {

    /**
     * 현재 시각 기준 활성 제재를 최신 순으로 1건 조회한다.
     *
     * @param userId 사용자 식별자
     * @param now 시작 시각 비교 기준
     * @param now2 종료 시각 비교 기준
     * @return 활성 제재 엔티티 조회 결과
     */
    Optional<UserRestrictionEntity> findFirstByUserIdAndStartAtLessThanEqualAndEndAtGreaterThanEqualOrderByIdDesc(
        Long userId,
        LocalDateTime now,
        LocalDateTime now2
    );
}
