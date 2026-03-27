package lab.coder.colly.domain.auth.adapter.out.persistence.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import lab.coder.colly.domain.auth.adapter.out.persistence.entity.AuthMagicLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthMagicLinkJpaRepository extends JpaRepository<AuthMagicLinkEntity, Long> {

    Optional<AuthMagicLinkEntity> findByTokenHash(String tokenHash);

    boolean existsByEmailAndCreatedAtGreaterThanEqual(String email, LocalDateTime afterTime);
}
