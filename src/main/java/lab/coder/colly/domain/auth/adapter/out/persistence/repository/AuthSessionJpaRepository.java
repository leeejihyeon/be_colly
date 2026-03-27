package lab.coder.colly.domain.auth.adapter.out.persistence.repository;

import lab.coder.colly.domain.auth.adapter.out.persistence.entity.AuthSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 인증 세션 엔티티 조회용 JPA 리포지토리.
 */
public interface AuthSessionJpaRepository extends JpaRepository<AuthSessionEntity, Long> {
}
