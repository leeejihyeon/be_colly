package lab.coder.colly.domain.auth.adapter.out.persistence.repository;

import lab.coder.colly.domain.auth.adapter.out.persistence.entity.AuthSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthSessionJpaRepository extends JpaRepository<AuthSessionEntity, Long> {
}
