package lab.coder.colly.domain.auth.adapter.out.persistence.repository;

import java.util.Optional;
import lab.coder.colly.domain.auth.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
}
