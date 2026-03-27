package lab.coder.colly.domain.community.adapter.out.persistence.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import lab.coder.colly.domain.community.adapter.out.persistence.entity.UserRestrictionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRestrictionJpaRepository extends JpaRepository<UserRestrictionEntity, Long> {

    Optional<UserRestrictionEntity> findFirstByUserIdAndStartAtLessThanEqualAndEndAtGreaterThanEqualOrderByIdDesc(
        Long userId,
        LocalDateTime now,
        LocalDateTime now2
    );
}
