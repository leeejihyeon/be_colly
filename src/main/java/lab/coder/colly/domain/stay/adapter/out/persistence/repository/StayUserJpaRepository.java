package lab.coder.colly.domain.stay.adapter.out.persistence.repository;

import lab.coder.colly.domain.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StayUserJpaRepository extends JpaRepository<UserEntity, Long> {
}
