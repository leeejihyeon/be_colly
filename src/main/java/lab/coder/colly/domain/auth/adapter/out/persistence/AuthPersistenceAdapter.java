package lab.coder.colly.domain.auth.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.Optional;
import lab.coder.colly.domain.auth.adapter.out.persistence.entity.AuthMagicLinkEntity;
import lab.coder.colly.domain.auth.adapter.out.persistence.entity.AuthSessionEntity;
import lab.coder.colly.domain.auth.adapter.out.persistence.entity.UserEntity;
import lab.coder.colly.domain.auth.adapter.out.persistence.repository.AuthMagicLinkJpaRepository;
import lab.coder.colly.domain.auth.adapter.out.persistence.repository.AuthSessionJpaRepository;
import lab.coder.colly.domain.auth.adapter.out.persistence.repository.AuthUserJpaRepository;
import lab.coder.colly.domain.auth.application.port.out.AuthMagicLinkPort;
import lab.coder.colly.domain.auth.application.port.out.AuthSessionPort;
import lab.coder.colly.domain.auth.application.port.out.AuthUserPort;
import lab.coder.colly.domain.auth.domain.model.AuthMagicLink;
import lab.coder.colly.domain.auth.domain.model.AuthSession;
import lab.coder.colly.domain.auth.domain.model.AuthUser;
import org.springframework.stereotype.Component;

@Component
public class AuthPersistenceAdapter implements AuthMagicLinkPort, AuthUserPort, AuthSessionPort {

    private final AuthMagicLinkJpaRepository authMagicLinkJpaRepository;
    private final AuthUserJpaRepository authUserJpaRepository;
    private final AuthSessionJpaRepository authSessionJpaRepository;

    public AuthPersistenceAdapter(
        AuthMagicLinkJpaRepository authMagicLinkJpaRepository,
        AuthUserJpaRepository authUserJpaRepository,
        AuthSessionJpaRepository authSessionJpaRepository
    ) {
        this.authMagicLinkJpaRepository = authMagicLinkJpaRepository;
        this.authUserJpaRepository = authUserJpaRepository;
        this.authSessionJpaRepository = authSessionJpaRepository;
    }

    /**
     * 매직링크 도메인 모델을 저장한다.
     *
     * @param magicLink 저장할 매직링크
     * @return 저장된 매직링크
     */
    @Override
    public AuthMagicLink save(AuthMagicLink magicLink) {
        AuthMagicLinkEntity saved = authMagicLinkJpaRepository.save(new AuthMagicLinkEntity(
            magicLink.getId(),
            magicLink.getEmail(),
            magicLink.getTokenHash(),
            magicLink.getExpiresAt(),
            magicLink.getUsedAt()
        ));
        return AuthMagicLink.restore(saved.getId(), saved.getEmail(), saved.getTokenHash(), saved.getExpiresAt(), saved.getUsedAt());
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
            .map(entity -> AuthMagicLink.restore(entity.getId(), entity.getEmail(), entity.getTokenHash(), entity.getExpiresAt(), entity.getUsedAt()));
    }

    /**
     * 지정 시각 이후 동일 이메일 발급 이력 존재 여부를 조회한다.
     *
     * @param email 이메일 주소
     * @param afterTime 기준 시각
     * @return 최근 발급 이력 존재 여부
     */
    @Override
    public boolean existsRecentByEmail(String email, LocalDateTime afterTime) {
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
        return authUserJpaRepository.findByEmail(email).map(this::toDomain);
    }

    /**
     * 사용자 계정을 저장한다.
     *
     * @param user 저장할 사용자
     * @return 저장된 사용자
     */
    @Override
    public AuthUser save(AuthUser user) {
        UserEntity saved = authUserJpaRepository.save(new UserEntity(user.getId(), user.getEmail(), user.getName()));
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
        AuthSessionEntity saved = authSessionJpaRepository.save(
            new AuthSessionEntity(session.getId(), session.getUserId(), session.getRefreshTokenHash(), session.getExpiresAt())
        );
        return AuthSession.restore(saved.getId(), saved.getUserId(), saved.getRefreshTokenHash(), saved.getExpiresAt());
    }

    /**
     * JPA 사용자 엔티티를 도메인 사용자 모델로 변환한다.
     *
     * @param entity 사용자 JPA 엔티티
     * @return 도메인 사용자 모델
     */
    private AuthUser toDomain(UserEntity entity) {
        return AuthUser.restore(entity.getId(), entity.getEmail(), entity.getName());
    }
}
