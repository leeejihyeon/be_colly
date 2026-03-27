package lab.coder.colly.domain.auth.adapter.out.persistence.repository;

import java.util.Optional;
import lab.coder.colly.domain.auth.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 인증 도메인 사용자 엔티티 조회용 JPA 리포지토리.
 */
public interface AuthUserJpaRepository extends JpaRepository<UserEntity, Long> {

    /**
     * 이메일로 사용자 엔티티를 조회한다.
     *
     * @param email 사용자 이메일
     * @return 사용자 엔티티 조회 결과
     */
    Optional<UserEntity> findByEmail(String email);
}
