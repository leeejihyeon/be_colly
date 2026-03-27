package lab.coder.colly.domain.auth.adapter.out.persistence;

import lab.coder.colly.domain.auth.adapter.out.persistence.entity.AuthIdentityEntity;
import lab.coder.colly.domain.auth.adapter.out.persistence.entity.AuthMagicLinkEntity;
import lab.coder.colly.domain.auth.adapter.out.persistence.entity.AuthSessionEntity;
import lab.coder.colly.domain.auth.adapter.out.persistence.entity.UserEntity;
import lab.coder.colly.domain.auth.adapter.out.persistence.repository.AuthIdentityJpaRepository;
import lab.coder.colly.domain.auth.adapter.out.persistence.repository.AuthMagicLinkJpaRepository;
import lab.coder.colly.domain.auth.adapter.out.persistence.repository.AuthSessionJpaRepository;
import lab.coder.colly.domain.auth.adapter.out.persistence.repository.AuthUserJpaRepository;
import lab.coder.colly.domain.auth.application.port.out.AuthIdentityPort;
import lab.coder.colly.domain.auth.application.port.out.AuthMagicLinkPort;
import lab.coder.colly.domain.auth.application.port.out.AuthSessionPort;
import lab.coder.colly.domain.auth.application.port.out.AuthUserPort;
import lab.coder.colly.domain.auth.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 인증 도메인의 영속성 어댑터.
 *
 * <p>
 * Auth 관련 아웃바운드 포트(AuthMagicLinkPort / AuthUserPort / AuthSessionPort / AuthIdentityPort)를 구현하며,
 * <p>
 * 도메인 모델과 JPA 엔티티 간 변환 및 DB 접근을 담당한다.
 */
@Component
@RequiredArgsConstructor
public class AuthPersistenceAdapter implements AuthMagicLinkPort, AuthUserPort, AuthSessionPort, AuthIdentityPort {

    private final AuthIdentityJpaRepository authIdentityJpaRepository;
    private final AuthMagicLinkJpaRepository authMagicLinkJpaRepository;
    private final AuthUserJpaRepository authUserJpaRepository;
    private final AuthSessionJpaRepository authSessionJpaRepository;

    /**
     * 매직링크 도메인 모델을 저장한다.
     *
     * @param magicLink 저장할 매직링크
     * @return 저장된 매직링크
     */
    @Override
    public AuthMagicLink save(AuthMagicLink magicLink) {

        AuthMagicLinkEntity saved =
                authMagicLinkJpaRepository.save(
                        new AuthMagicLinkEntity(
                                magicLink.getId(),
                                magicLink.getEmail(),
                                magicLink.getTokenHash(),
                                magicLink.getExpiresAt(),
                                magicLink.getUsedAt()
                        )
                );
        return AuthMagicLink.restore(
                saved.getId(),
                saved.getEmail(),
                saved.getTokenHash(),
                saved.getExpiresAt(),
                saved.getUsedAt()
        );
    }

    /**
     * 토큰 해시로 매직링크를 조회한다.
     *
     * @param tokenHash 매직링크 토큰 해시
     * @return 조회 결과
     */
    @Override
    public Optional<AuthMagicLink> findByTokenHash(String tokenHash) {

        return authMagicLinkJpaRepository.findByTokenHash(tokenHash)
                .map(entity ->
                        AuthMagicLink.restore(
                                entity.getId(),
                                entity.getEmail(),
                                entity.getTokenHash(),
                                entity.getExpiresAt(),
                                entity.getUsedAt()
                        )
                );
    }

    /**
     * 지정 시각 이후 동일 이메일 발급 이력 존재 여부를 조회한다.
     *
     * @param email     이메일 주소
     * @param afterTime 기준 시각
     * @return 최근 발급 이력 존재 여부
     */
    @Override
    public boolean existsRecentByEmail(
            String email,
            LocalDateTime afterTime
    ) {
        return authMagicLinkJpaRepository.existsByEmailAndCreatedAtGreaterThanEqual(email, afterTime);
    }

    /**
     * 이메일로 사용자 계정을 조회한다.
     *
     * @param email 사용자 이메일
     * @return 사용자 조회 결과
     */
    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return authUserJpaRepository.findByEmail(email)
                .map(this::toDomain);
    }

    /**
     * 사용자 식별자로 사용자 계정을 조회한다.
     *
     * @param userId 사용자 식별자
     * @return 사용자 조회 결과
     */
    @Override
    public Optional<AuthUser> findById(Long userId) {
        return authUserJpaRepository.findById(userId)
                .map(this::toDomain);
    }

    /**
     * 사용자 계정을 저장한다.
     *
     * @param user 저장할 사용자
     * @return 저장된 사용자
     */
    @Override
    public AuthUser save(AuthUser user) {

        UserEntity saved = authUserJpaRepository.save(
                new UserEntity(
                        user.getId(),
                        user.getEmail(),
                        user.getName()
                )
        );

        return toDomain(saved);
    }

    /**
     * 인증 세션을 저장한다.
     *
     * @param session 저장할 인증 세션
     * @return 저장된 인증 세션
     */
    @Override
    public AuthSession save(AuthSession session) {

        AuthSessionEntity saved =
                authSessionJpaRepository.save(
                        new AuthSessionEntity(
                                session.getId(),
                                session.getUserId(),
                                session.getRefreshTokenHash(),
                                session.getExpiresAt()
                        )
                );

        return AuthSession.restore(
                saved.getId(),
                saved.getUserId(),
                saved.getRefreshTokenHash(),
                saved.getExpiresAt()
        );
    }

    /**
     * 소셜 제공자 식별자로 인증 수단 연결 정보를 조회한다.
     *
     * @param provider       인증 제공자
     * @param providerUserId 제공자 사용자 식별자
     * @return 인증 수단 조회 결과
     */
    @Override
    public Optional<AuthIdentity> findByProviderAndProviderUserId(
            AuthProvider provider,
            String providerUserId
    ) {
        return authIdentityJpaRepository.findByProviderAndProviderUserId(
                provider,
                providerUserId
        ).map(this::toDomain);
    }

    /**
     * 인증 수단 연결 정보를 저장한다.
     *
     * @param identity 저장할 인증 수단
     * @return 저장된 인증 수단
     */
    @Override
    public AuthIdentity save(AuthIdentity identity) {
        AuthIdentityEntity saved =
                authIdentityJpaRepository.save(
                        new AuthIdentityEntity(
                                identity.getId(),
                                identity.getUserId(),
                                identity.getProvider(),
                                identity.getProviderUserId()
                        )
                );

        return toDomain(saved);
    }

    /**
     * JPA 사용자 엔티티를 도메인 사용자 모델로 변환한다.
     *
     * @param entity 사용자 JPA 엔티티
     * @return 도메인 사용자 모델
     */
    private AuthUser toDomain(UserEntity entity) {

        return AuthUser.restore(
                entity.getId(),
                entity.getEmail(),
                entity.getName()
        );
    }

    /**
     * 인증 수단 JPA 엔티티를 도메인 모델로 변환한다.
     *
     * @param entity 인증 수단 JPA 엔티티
     * @return 인증 수단 도메인 모델
     */
    private AuthIdentity toDomain(AuthIdentityEntity entity) {

        return AuthIdentity.restore(
                entity.getId(),
                entity.getUserId(),
                entity.getProvider(),
                entity.getProviderUserId()
        );
    }
}
