package lab.coder.colly.domain.auth.application.service;

import lab.coder.colly.domain.auth.application.port.in.IssueMagicLinkUseCase;
import lab.coder.colly.domain.auth.application.port.in.SocialLoginUseCase;
import lab.coder.colly.domain.auth.application.port.in.VerifyMagicLinkUseCase;
import lab.coder.colly.domain.auth.application.port.out.AuthIdentityPort;
import lab.coder.colly.domain.auth.application.port.out.AuthMagicLinkPort;
import lab.coder.colly.domain.auth.application.port.out.AuthSessionPort;
import lab.coder.colly.domain.auth.application.port.out.AuthUserPort;
import lab.coder.colly.domain.auth.domain.model.*;
import lab.coder.colly.domain.auth.domain.policy.AuthEmailPolicy;
import lab.coder.colly.domain.auth.support.AuthTokenGenerator;
import lab.coder.colly.domain.auth.support.HashSupport;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 인증 유스케이스 구현 서비스.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService implements IssueMagicLinkUseCase, VerifyMagicLinkUseCase, SocialLoginUseCase {

    private static final long MAGIC_LINK_EXPIRE_SECONDS = 15 * 60;
    private static final long MAGIC_LINK_COOLDOWN_SECONDS = 60;

    private final AuthMagicLinkPort authMagicLinkPort;
    private final AuthUserPort authUserPort;
    private final AuthSessionPort authSessionPort;
    private final AuthIdentityPort authIdentityPort;

    /**
     * 이메일 기반 매직링크 발급 요청을 처리한다.
     *
     * @param command 발급 요청 정보(이메일)
     * @return 발급 결과(정규화된 이메일, 만료 시간, 토큰)
     */
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

        return new MagicLinkIssueResult(
                magicLink.getEmail(),
                MAGIC_LINK_EXPIRE_SECONDS,
                rawToken
        );
    }

    /**
     * 매직링크 토큰을 검증하고 로그인 세션 토큰을 발급한다.
     *
     * @param command 검증 요청 정보(매직 토큰)
     * @return 로그인 결과(사용자 식별자 및 세션 토큰)
     */
    @Override
    @Transactional
    public LoginResult verify(VerifyMagicLinkCommand command) {

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

        return issueLoginResult(user);
    }

    /**
     * 소셜 제공자 계정으로 로그인 또는 회원가입을 처리한다.
     *
     * @param command 소셜 로그인 요청 정보
     * @return 로그인 결과
     */
    @Override
    @Transactional
    public SocialLoginResult login(SocialLoginCommand command) {

        if (command.provider() == null) {
            throw new DomainException(
                    ErrorCode.AUTH_INVALID_PROVIDER,
                    "Social provider is required"
            );
        }

        if (command.providerUserId() == null || command.providerUserId().isBlank()) {
            throw new DomainException(
                    ErrorCode.AUTH_INVALID_PROVIDER_USER_ID,
                    "Provider user id is required"
            );
        }

        String email = AuthEmailPolicy.normalizeAndValidate(command.email());
        String providerUserId = command.providerUserId().trim();

        AuthUser user = authIdentityPort
                .findByProviderAndProviderUserId(command.provider(), providerUserId)
                .flatMap(identity ->
                        authUserPort.findById(identity.getUserId())
                )
                .orElseGet(() ->
                        linkOrCreateSocialUser(
                                command.provider(),
                                providerUserId,
                                email,
                                command.name()
                        )
                );

        LoginResult result = issueLoginResult(user);

        return new SocialLoginResult(
                result.userId(),
                result.email(),
                result.accessToken(),
                result.refreshToken()
        );
    }

    /**
     * 소셜 로그인 시 사용자 생성/연결을 처리한다.
     *
     * @param provider       인증 제공자
     * @param providerUserId 제공자 사용자 식별자
     * @param email          사용자 이메일
     * @param name           사용자 표시 이름
     * @return 연결된 사용자
     */
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

    /**
     * 사용자 기준으로 액세스/리프레시 토큰을 발급한다.
     *
     * @param user 로그인 대상 사용자
     * @return 로그인 결과
     */
    private LoginResult issueLoginResult(AuthUser user) {

        String accessToken = AuthTokenGenerator.randomToken(32);
        String refreshToken = AuthTokenGenerator.randomToken(32);

        authSessionPort.save(
                AuthSession.create(
                        user.getId(),
                        HashSupport.sha256(refreshToken),
                        LocalDateTime.now().plusDays(14)
                )
        );

        return new LoginResult(
                user.getId(),
                user.getEmail(),
                accessToken,
                refreshToken
        );
    }
}
