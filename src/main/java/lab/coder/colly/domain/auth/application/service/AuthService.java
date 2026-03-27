package lab.coder.colly.domain.auth.application.service;

import java.time.LocalDateTime;
import lab.coder.colly.domain.auth.application.port.in.RequestMagicLinkUseCase;
import lab.coder.colly.domain.auth.application.port.in.VerifyMagicLinkUseCase;
import lab.coder.colly.domain.auth.application.port.out.AuthMagicLinkPort;
import lab.coder.colly.domain.auth.application.port.out.AuthSessionPort;
import lab.coder.colly.domain.auth.application.port.out.AuthUserPort;
import lab.coder.colly.domain.auth.domain.model.AuthMagicLink;
import lab.coder.colly.domain.auth.domain.model.AuthSession;
import lab.coder.colly.domain.auth.domain.model.AuthUser;
import lab.coder.colly.domain.auth.domain.policy.AuthEmailPolicy;
import lab.coder.colly.domain.auth.support.AuthTokenGenerator;
import lab.coder.colly.domain.auth.support.HashSupport;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService implements RequestMagicLinkUseCase, VerifyMagicLinkUseCase {

    private static final long MAGIC_LINK_EXPIRE_SECONDS = 15 * 60;
    private static final long MAGIC_LINK_COOLDOWN_SECONDS = 60;

    private final AuthMagicLinkPort authMagicLinkPort;
    private final AuthUserPort authUserPort;
    private final AuthSessionPort authSessionPort;

    public AuthService(AuthMagicLinkPort authMagicLinkPort, AuthUserPort authUserPort, AuthSessionPort authSessionPort) {
        this.authMagicLinkPort = authMagicLinkPort;
        this.authUserPort = authUserPort;
        this.authSessionPort = authSessionPort;
    }

    /**
     * 이메일 기반 매직링크 발급 요청을 처리한다.
     *
     * @param command 발급 요청 정보(이메일)
     * @return 발급 결과(정규화된 이메일, 만료 시간, 토큰)
     */
    @Override
    @Transactional
    public MagicLinkRequestResult request(MagicLinkRequestCommand command) {
        String email = AuthEmailPolicy.normalizeAndValidate(command.email());

        boolean requestedRecently = authMagicLinkPort.existsRecentByEmail(email, LocalDateTime.now().minusSeconds(MAGIC_LINK_COOLDOWN_SECONDS));
        if (requestedRecently) {
            throw new DomainException(ErrorCode.MAGIC_LINK_RATE_LIMITED, "Magic link requested too frequently");
        }

        String rawToken = AuthTokenGenerator.randomToken(24);
        String tokenHash = HashSupport.sha256(rawToken);
        AuthMagicLink magicLink = authMagicLinkPort.save(AuthMagicLink.issue(email, tokenHash, LocalDateTime.now().plusSeconds(MAGIC_LINK_EXPIRE_SECONDS)));

        return new MagicLinkRequestResult(magicLink.getEmail(), MAGIC_LINK_EXPIRE_SECONDS, rawToken);
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
            throw new DomainException(ErrorCode.MAGIC_LINK_NOT_FOUND, "Magic token is required");
        }

        AuthMagicLink magicLink = authMagicLinkPort.findByTokenHash(HashSupport.sha256(command.token()))
            .orElseThrow(() -> new DomainException(ErrorCode.MAGIC_LINK_NOT_FOUND, "Magic link not found"));

        if (magicLink.isUsed()) {
            throw new DomainException(ErrorCode.MAGIC_LINK_ALREADY_USED, "Magic link already used");
        }

        if (magicLink.isExpiredAt(LocalDateTime.now())) {
            throw new DomainException(ErrorCode.MAGIC_LINK_EXPIRED, "Magic link expired");
        }

        authMagicLinkPort.save(magicLink.markUsed(LocalDateTime.now()));

        AuthUser user = authUserPort.findByEmail(magicLink.getEmail())
            .orElseGet(() -> authUserPort.save(AuthUser.create(
                magicLink.getEmail(),
                AuthEmailPolicy.defaultDisplayName(magicLink.getEmail())
            )));

        String accessToken = AuthTokenGenerator.randomToken(32);
        String refreshToken = AuthTokenGenerator.randomToken(32);
        authSessionPort.save(AuthSession.create(user.getId(), HashSupport.sha256(refreshToken), LocalDateTime.now().plusDays(14)));

        return new LoginResult(user.getId(), user.getEmail(), accessToken, refreshToken);
    }
}
