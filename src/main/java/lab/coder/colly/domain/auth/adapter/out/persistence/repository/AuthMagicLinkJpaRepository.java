package lab.coder.colly.domain.auth.adapter.out.persistence.repository;

import lab.coder.colly.domain.auth.adapter.out.persistence.entity.AuthMagicLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 매직링크 인증 엔티티 조회용 JPA 리포지토리.
 */
public interface AuthMagicLinkJpaRepository extends JpaRepository<AuthMagicLinkEntity, Long> {

    /**
     * 토큰 해시로 매직링크 엔티티를 조회한다.
     *
     * @param tokenHash 매직링크 토큰 해시
     * @return 매직링크 엔티티 조회 결과
     */
    Optional<AuthMagicLinkEntity> findByTokenHash(String tokenHash);

    /**
     * 특정 시각 이후 동일 이메일의 매직링크 발급 이력 존재 여부를 조회한다.
     *
     * @param email     로그인 요청 이메일
     * @param afterTime 기준 시각
     * @return 최근 발급 이력 존재 여부
     */
    boolean existsByEmailAndCreatedAtGreaterThanEqual(
            String email,
            LocalDateTime afterTime
    );
}
