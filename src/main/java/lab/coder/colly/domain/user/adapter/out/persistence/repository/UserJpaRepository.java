package lab.coder.colly.domain.user.adapter.out.persistence.repository;

import lab.coder.colly.domain.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 사용자 엔티티 조회용 JPA 리포지토리.
 */
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    /**
     * 이메일 중복 여부를 조회한다.
     *
     * @param email 사용자 이메일
     * @return 이메일 중복 여부
     */
    boolean existsByEmail(String email);
}
