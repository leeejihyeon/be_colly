package lab.coder.colly.domain.auth.adapter.out.persistence.repository;

import lab.coder.colly.domain.auth.adapter.out.persistence.entity.AuthSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 인증 세션 엔티티 조회용 JPA 리포지토리.
 */
public interface AuthSessionJpaRepository extends JpaRepository<AuthSessionEntity, Long> {

    Optional<AuthSessionEntity> findByRefreshTokenHash(String refreshTokenHash);

    void deleteByRefreshTokenHash(String refreshTokenHash);

    void deleteByUserId(Long userId);

    void deleteByExpiresAtLessThanEqual(LocalDateTime now);
}
