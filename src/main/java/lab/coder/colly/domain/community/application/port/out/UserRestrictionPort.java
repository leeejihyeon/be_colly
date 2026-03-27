package lab.coder.colly.domain.community.application.port.out;

import java.time.LocalDateTime;
import java.util.Optional;
import lab.coder.colly.domain.community.domain.model.UserRestriction;

/**
 * 사용자 제재 저장/조회 아웃바운드 포트.
 */
public interface UserRestrictionPort {

    /**
     * 사용자 제재 정보를 저장한다.
     *
     * @param restriction 저장할 사용자 제재 정보
     * @return 저장된 사용자 제재 정보
     */
    UserRestriction save(UserRestriction restriction);

    /**
     * 기준 시각의 활성 사용자 제재를 조회한다.
     *
     * @param userId 사용자 식별자
     * @param now 기준 시각
     * @return 활성 사용자 제재 조회 결과
     */
    Optional<UserRestriction> findActiveByUserId(Long userId, LocalDateTime now);
}
