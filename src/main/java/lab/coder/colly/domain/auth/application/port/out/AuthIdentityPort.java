package lab.coder.colly.domain.auth.application.port.out;

import lab.coder.colly.domain.auth.domain.model.AuthIdentity;
import lab.coder.colly.domain.auth.domain.model.AuthProvider;

import java.util.Optional;

/**
 * 인증 수단 식별자 저장 포트.
 */
public interface AuthIdentityPort {

    /**
     * 제공자/제공자 사용자 식별자로 인증 식별자를 조회한다.
     *
     * @param provider       인증 제공자
     * @param providerUserId 제공자 내 사용자 식별자
     * @return 인증 식별자 조회 결과
     */
    Optional<AuthIdentity> findByProviderAndProviderUserId(
            AuthProvider provider,
            String providerUserId
    );

    /**
     * 인증 식별자를 저장한다.
     *
     * @param identity 저장할 인증 식별자
     * @return 저장된 인증 식별자
     */
    AuthIdentity save(AuthIdentity identity);
}
