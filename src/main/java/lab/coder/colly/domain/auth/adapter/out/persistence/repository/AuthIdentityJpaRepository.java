package lab.coder.colly.domain.auth.adapter.out.persistence.repository;

import lab.coder.colly.domain.auth.adapter.out.persistence.entity.AuthIdentityEntity;
import lab.coder.colly.domain.auth.domain.model.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 사용자 인증 수단 연결 엔티티 조회용 JPA 리포지토리.
 */
public interface AuthIdentityJpaRepository extends JpaRepository<AuthIdentityEntity, Long> {

    /**
     * 로그인하는 소셜 서비스(provider)와 그 서비스의 사용자 ID로 계정 연결 정보를 조회한다.
     *
     * @param provider       인증 제공자
     * @param providerUserId 제공자 사용자 식별자
     * @return 인증 수단 연결 엔티티 조회 결과
     */
    Optional<AuthIdentityEntity> findByProviderAndProviderUserId(
            AuthProvider provider,
            String providerUserId
    );
}
