package lab.coder.colly.domain.auth.application.service;

import java.time.LocalDateTime;
import lab.coder.colly.domain.auth.application.port.in.GetCurrentUserUseCase;
import lab.coder.colly.domain.auth.application.port.in.IssueMagicLinkUseCase;
import lab.coder.colly.domain.auth.application.port.in.RefreshTokenUseCase;
import lab.coder.colly.domain.auth.application.port.in.SignOutAllUseCase;
import lab.coder.colly.domain.auth.application.port.in.SignOutUseCase;
import lab.coder.colly.domain.auth.application.port.in.SocialLoginUseCase;
import lab.coder.colly.domain.auth.application.port.in.VerifyMagicLinkUseCase;
import lab.coder.colly.domain.auth.application.port.out.AuthIdentityPort;
import lab.coder.colly.domain.auth.application.port.out.AuthMagicLinkMailPort;
import lab.coder.colly.domain.auth.application.port.out.AuthMagicLinkPort;
import lab.coder.colly.domain.auth.application.port.out.AuthSessionPort;
import lab.coder.colly.domain.auth.application.port.out.AuthUserPort;
import lab.coder.colly.domain.auth.domain.model.AuthIdentity;
import lab.coder.colly.domain.auth.domain.model.AuthMagicLink;
import lab.coder.colly.domain.auth.domain.model.AuthProvider;
import lab.coder.colly.domain.auth.domain.model.AuthSession;
import lab.coder.colly.domain.auth.domain.model.AuthUser;
import lab.coder.colly.domain.auth.domain.policy.AuthEmailPolicy;
import lab.coder.colly.domain.auth.support.AuthTokenGenerator;
import lab.coder.colly.domain.auth.support.HashSupport;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;
import lab.coder.colly.shared.security.AuthJwtProperties;
import lab.coder.colly.shared.security.AuthJwtTokenService;
import lab.coder.colly.shared.security.AuthSocialProfile;
import lab.coder.colly.shared.security.AuthSocialTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 유스케이스 구현 서비스.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService implements
        IssueMagicLinkUseCase,
        VerifyMagicLinkUseCase,
        SocialLoginUseCase,
        RefreshTokenUseCase,
        SignOutUseCase,
        SignOutAllUseCase,
        GetCurrentUserUseCase {

    private static final long MAGIC_LINK_EXPIRE_SECONDS = 15 * 60;
    private static final long MAGIC_LINK_COOLDOWN_SECONDS = 60;

    private final AuthMagicLinkPort authMagicLinkPort;
    private final AuthMagicLinkMailPort authMagicLinkMailPort;
    private final AuthUserPort authUserPort;
    private final AuthSessionPort authSessionPort;
    private final AuthIdentityPort authIdentityPort;
    private final AuthJwtTokenService authJwtTokenService;
    private final AuthJwtProperties authJwtProperties;
    private final AuthSocialTokenVerifier authSocialTokenVerifier;

    @Override
    @Transactional
    public MagicLinkIssueResult issue(MagicLinkIssueCommand command) {
        String email = AuthEmailPolicy.normalizeAndValidate(command.email());

        boolean requestedRecently =
                authMagicLinkPort.existsRecentByEmail(
                        email,
                        LocalDateTime.now().minusSeconds(MAGIC_LINK_COOLDOWN_SECONDS)
                );

        if (requestedRecently) {
            throw new DomainException(
                    ErrorCode.MAGIC_LINK_RATE_LIMITED,
                    "Magic link requested too frequently"
            );
        }

        String rawToken = AuthTokenGenerator.randomToken(24);
        String tokenHash = HashSupport.sha256(rawToken);

        AuthMagicLink magicLink =
                authMagicLinkPort.save(
                        AuthMagicLink.issue(
                                email,
                                tokenHash,
                                LocalDateTime.now().plusSeconds(MAGIC_LINK_EXPIRE_SECONDS)
                        )
                );

        authMagicLinkMailPort.sendMagicLink(magicLink.getEmail(), rawToken, MAGIC_LINK_EXPIRE_SECONDS);

        return new MagicLinkIssueResult(
                magicLink.getEmail(),
                MAGIC_LINK_EXPIRE_SECONDS
        );
    }

    @Override
    @Transactional
    public VerifyMagicLinkUseCase.LoginResult verify(VerifyMagicLinkCommand command) {
        if (command.token() == null || command.token().isBlank()) {
            throw new DomainException(
                    ErrorCode.MAGIC_LINK_NOT_FOUND,
                    "Magic token is required"
            );
        }

        AuthMagicLink magicLink =
                authMagicLinkPort.findByTokenHash(HashSupport.sha256(command.token()))
                        .orElseThrow(() ->
                                new DomainException(
                                        ErrorCode.MAGIC_LINK_NOT_FOUND,
                                        "Magic link not found")
                        );

        if (magicLink.isUsed()) {
            throw new DomainException(
                    ErrorCode.MAGIC_LINK_ALREADY_USED,
                    "Magic link already used"
            );
        }

        if (magicLink.isExpiredAt(LocalDateTime.now())) {
            throw new DomainException(
                    ErrorCode.MAGIC_LINK_EXPIRED,
                    "Magic link expired"
            );
        }

        authMagicLinkPort.save(magicLink.markUsed(LocalDateTime.now()));

        AuthUser user = authUserPort.findByEmail(magicLink.getEmail())
                .orElseGet(() ->
                        authUserPort.save(
                                AuthUser.create(
                                        magicLink.getEmail(),
                                        AuthEmailPolicy.defaultDisplayName(magicLink.getEmail())
                                )
                        )
                );

        return issueLoginResult(user, AuthProvider.EMAIL_MAGIC_LINK);
    }

    @Override
    @Transactional
    public RefreshTokenUseCase.LoginResult refresh(RefreshTokenUseCase.RefreshTokenCommand command) {
        if (command.refreshToken() == null || command.refreshToken().isBlank()) {
            throw new DomainException(
                    ErrorCode.REFRESH_TOKEN_NOT_FOUND,
                    "Refresh token is required"
            );
        }

        String tokenHash = HashSupport.sha256(command.refreshToken());
        AuthSession session = authSessionPort.findByRefreshTokenHash(tokenHash)
                .orElseThrow(() ->
                        new DomainException(
                                ErrorCode.REFRESH_TOKEN_NOT_FOUND,
                                "Refresh token not found"
                        )
                );

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            authSessionPort.deleteByRefreshTokenHash(tokenHash);
            throw new DomainException(
                    ErrorCode.REFRESH_TOKEN_EXPIRED,
                    "Refresh token has expired"
            );
        }

        AuthUser user = authUserPort.findById(session.getUserId())
                .orElseThrow(() ->
                        new DomainException(
                                ErrorCode.USER_NOT_FOUND,
                                "User not found"
                        )
                );

        return issueLoginResultForRefresh(session, user);
    }

    @Override
    @Transactional
    public void signOut(SignOutUseCase.SignOutCommand command) {
        if (command.refreshToken() == null || command.refreshToken().isBlank()) {
            return;
        }

        authSessionPort.deleteByRefreshTokenHash(HashSupport.sha256(command.refreshToken()));
    }

    @Override
    @Transactional
    public void signOutAll(Long userId) {
        authSessionPort.deleteByUserId(userId);
    }

    @Override
    public CurrentUserView getCurrentUser(Long userId, AuthProvider provider) {
        AuthUser user = authUserPort.findById(userId)
                .orElseThrow(() ->
                        new DomainException(
                                ErrorCode.USER_NOT_FOUND,
                                "User not found"
                        )
                );

        return new CurrentUserView(
                user.getId(),
                user.getEmail(),
                provider
        );
    }

    @Override
    @Transactional
    public VerifyMagicLinkUseCase.LoginResult login(SocialLoginCommand command) {
        AuthSocialProfile socialProfile = switch (command.provider()) {
            case GOOGLE -> authSocialTokenVerifier.verifyGoogle(command.idToken());
            case APPLE -> authSocialTokenVerifier.verifyApple(command.idToken(), command.name());
            default -> throw new DomainException(
                    ErrorCode.AUTH_INVALID_PROVIDER,
                    "Unsupported provider: " + command.provider()
            );
        };

        String normalizedEmail = normalizeSocialEmail(socialProfile);

        AuthUser user = authIdentityPort
                .findByProviderAndProviderUserId(socialProfile.provider(), socialProfile.providerUserId())
                .flatMap(identity -> authUserPort.findById(identity.getUserId()))
                .orElseGet(() ->
                        linkOrCreateSocialUser(
                                socialProfile.provider(),
                                socialProfile.providerUserId(),
                                normalizedEmail,
                                socialProfile.name()
                        )
                );

        return issueLoginResult(user, socialProfile.provider());
    }

    private String normalizeSocialEmail(AuthSocialProfile socialProfile) {
        if (socialProfile.email() == null || socialProfile.email().isBlank()) {
            return AuthEmailPolicy.normalizeAndValidate(
                    socialProfile.provider().name().toLowerCase() + "-" + socialProfile.providerUserId() + "@users.colly.local"
            );
        }

        return AuthEmailPolicy.normalizeAndValidate(socialProfile.email());
    }

    private AuthUser linkOrCreateSocialUser(
            AuthProvider provider,
            String providerUserId,
            String email,
            String name
    ) {
        AuthUser user = authUserPort.findByEmail(email)
                .orElseGet(() ->
                        authUserPort.save(
                                AuthUser.create(
                                        email,
                                        (name == null || name.isBlank())
                                                ? AuthEmailPolicy.defaultDisplayName(email)
                                                : name.trim()
                                )
                        )
                );

        authIdentityPort.findByProviderAndProviderUserId(provider, providerUserId)
                .orElseGet(() ->
                        authIdentityPort.save(
                                AuthIdentity.create(
                                        user.getId(),
                                        provider,
                                        providerUserId
                                )
                        )
                );

        return user;
    }

    private VerifyMagicLinkUseCase.LoginResult issueLoginResult(
            AuthUser user,
            AuthProvider provider
    ) {
        authSessionPort.deleteExpiredSessions();

        String refreshToken = AuthTokenGenerator.randomToken(32);
        AuthSession savedSession = authSessionPort.save(
                AuthSession.create(
                        user.getId(),
                        provider,
                        HashSupport.sha256(refreshToken),
                        LocalDateTime.now().plusSeconds(authJwtProperties.resolvedRefreshTokenExpireSeconds())
                )
        );

        return createLoginResult(savedSession, user, refreshToken);
    }

    private RefreshTokenUseCase.LoginResult issueLoginResultForRefresh(
            AuthSession existingSession,
            AuthUser user
    ) {
        String refreshToken = AuthTokenGenerator.randomToken(32);
        AuthSession savedSession = authSessionPort.save(
                AuthSession.restore(
                        existingSession.getId(),
                        existingSession.getUserId(),
                        existingSession.getProvider(),
                        HashSupport.sha256(refreshToken),
                        LocalDateTime.now().plusSeconds(authJwtProperties.resolvedRefreshTokenExpireSeconds())
                )
        );

        VerifyMagicLinkUseCase.LoginResult loginResult = createLoginResult(savedSession, user, refreshToken);

        return new RefreshTokenUseCase.LoginResult(
                loginResult.userId(),
                loginResult.email(),
                loginResult.accessToken(),
                loginResult.refreshToken(),
                loginResult.expiresInSeconds(),
                loginResult.refreshExpiresInSeconds(),
                loginResult.provider()
        );
    }

    private VerifyMagicLinkUseCase.LoginResult createLoginResult(
            AuthSession session,
            AuthUser user,
            String refreshToken
    ) {
        String accessToken = authJwtTokenService.issueAccessToken(
                user.getId(),
                user.getEmail(),
                session.getProvider(),
                session.getId()
        );

        return new VerifyMagicLinkUseCase.LoginResult(
                user.getId(),
                user.getEmail(),
                accessToken,
                refreshToken,
                authJwtProperties.resolvedAccessTokenExpireSeconds(),
                authJwtProperties.resolvedRefreshTokenExpireSeconds(),
                session.getProvider()
        );
    }
}
