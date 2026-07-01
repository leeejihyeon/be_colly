package lab.coder.colly.domain.auth.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import lab.coder.colly.domain.auth.application.port.in.GetCurrentUserUseCase;
import lab.coder.colly.domain.auth.application.port.in.IssueMagicLinkUseCase;
import lab.coder.colly.domain.auth.application.port.in.RefreshTokenUseCase;
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
import lab.coder.colly.domain.auth.support.HashSupport;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;
import lab.coder.colly.shared.security.AuthJwtProperties;
import lab.coder.colly.shared.security.AuthJwtTokenService;
import lab.coder.colly.shared.security.AuthSocialProfile;
import lab.coder.colly.shared.security.AuthSocialTokenVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthMagicLinkPort authMagicLinkPort;

    @Mock
    private AuthMagicLinkMailPort authMagicLinkMailPort;

    @Mock
    private AuthUserPort authUserPort;

    @Mock
    private AuthSessionPort authSessionPort;

    @Mock
    private AuthIdentityPort authIdentityPort;

    @Mock
    private AuthJwtTokenService authJwtTokenService;

    @Mock
    private AuthJwtProperties authJwtProperties;

    @Mock
    private AuthSocialTokenVerifier authSocialTokenVerifier;

    @InjectMocks
    private AuthService authService;

    @Test
    void issue_issuesMagicTokenAndHidesRawTokenFromResponse() {
        when(authMagicLinkPort.existsRecentByEmail(any(), any())).thenReturn(false);
        when(authMagicLinkPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        IssueMagicLinkUseCase.MagicLinkIssueResult result = authService.issue(
                new IssueMagicLinkUseCase.MagicLinkIssueCommand("hello@example.com")
        );

        assertThat(result.email()).isEqualTo("hello@example.com");
        assertThat(result.expiresInSeconds()).isEqualTo(900);
        verify(authMagicLinkMailPort).sendMagicLink("hello@example.com", any(String.class), 15 * 60);
    }

    @Test
    void issue_rejectsTooFrequentRequest() {
        when(authMagicLinkPort.existsRecentByEmail(any(), any())).thenReturn(true);

        assertThatThrownBy(
                () -> authService.issue(new IssueMagicLinkUseCase.MagicLinkIssueCommand("hello@example.com"))
        )
                .isInstanceOf(DomainException.class)
                .extracting(ex -> ((DomainException) ex).getErrorCode())
                .isEqualTo(ErrorCode.MAGIC_LINK_RATE_LIMITED);
    }

    @Test
    void verify_rejectsWhenMagicLinkIsExpired() {
        String token = "token-value";
        when(authMagicLinkPort.findByTokenHash(HashSupport.sha256(token))).thenReturn(Optional.of(
                AuthMagicLink.restore(1L, "a@b.com", HashSupport.sha256(token), LocalDateTime.now().minusSeconds(1), null)
        ));

        assertThatThrownBy(() -> authService.verify(new VerifyMagicLinkUseCase.VerifyMagicLinkCommand(token)))
                .isInstanceOf(DomainException.class)
                .extracting(ex -> ((DomainException) ex).getErrorCode())
                .isEqualTo(ErrorCode.MAGIC_LINK_EXPIRED);
    }

    @Test
    void verify_createsSessionAndReturnsTokens() {
        String token = "token-value";
        String tokenHash = HashSupport.sha256(token);

        when(authJwtProperties.resolvedAccessTokenExpireSeconds()).thenReturn(900L);
        when(authJwtProperties.resolvedRefreshTokenExpireSeconds()).thenReturn(2592000L);
        when(authJwtTokenService.issueAccessToken(10L, "new@user.com", AuthProvider.EMAIL_MAGIC_LINK, 101L))
                .thenReturn("access-jwt");
        when(authMagicLinkPort.findByTokenHash(tokenHash)).thenReturn(Optional.of(
                AuthMagicLink.restore(1L, "new@user.com", tokenHash, LocalDateTime.now().plusMinutes(5), null)
        ));
        when(authMagicLinkPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(authUserPort.findByEmail("new@user.com")).thenReturn(Optional.of(AuthUser.restore(10L, "new@user.com", "new")));
        when(authSessionPort.save(any(AuthSession.class))).thenAnswer(invocation ->
                AuthSession.restore(101L, 10L, AuthProvider.EMAIL_MAGIC_LINK, "hash", LocalDateTime.now().plusDays(30))
        );

        VerifyMagicLinkUseCase.LoginResult result = authService.verify(new VerifyMagicLinkUseCase.VerifyMagicLinkCommand(token));

        assertThat(result.userId()).isEqualTo(10L);
        assertThat(result.accessToken()).isEqualTo("access-jwt");
        assertThat(result.refreshToken()).isNotBlank();
        assertThat(result.provider()).isEqualTo(AuthProvider.EMAIL_MAGIC_LINK);
    }

    @Test
    void refresh_rotatesTokensWhenRefreshIsValid() {
        String refreshToken = "old-refresh-token";
        String oldRefreshHash = HashSupport.sha256(refreshToken);

        when(authJwtProperties.resolvedAccessTokenExpireSeconds()).thenReturn(900L);
        when(authJwtProperties.resolvedRefreshTokenExpireSeconds()).thenReturn(2592000L);
        when(authJwtTokenService.issueAccessToken(12L, "user@example.com", AuthProvider.GOOGLE, 11L))
                .thenReturn("refreshed-access-jwt");
        when(authSessionPort.findByRefreshTokenHash(oldRefreshHash)).thenReturn(
                Optional.of(
                        AuthSession.restore(
                                11L,
                                12L,
                                AuthProvider.GOOGLE,
                                oldRefreshHash,
                                LocalDateTime.now().plusHours(1)
                        )
                )
        );
        when(authUserPort.findById(12L)).thenReturn(Optional.of(AuthUser.restore(12L, "user@example.com", "User")));
        when(authSessionPort.save(any(AuthSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshTokenUseCase.LoginResult result = authService.refresh(
                new RefreshTokenUseCase.RefreshTokenCommand(refreshToken)
        );

        assertThat(result.userId()).isEqualTo(12L);
        assertThat(result.accessToken()).isEqualTo("refreshed-access-jwt");
        assertThat(result.refreshToken()).isNotBlank();
        assertThat(result.provider()).isEqualTo(AuthProvider.GOOGLE);
    }

    @Test
    void refresh_rejectsExpiredSession() {
        String refreshToken = "expired-refresh";
        when(authSessionPort.findByRefreshTokenHash(HashSupport.sha256(refreshToken))).thenReturn(
                Optional.of(AuthSession.restore(2L, 13L, AuthProvider.GOOGLE, HashSupport.sha256(refreshToken), LocalDateTime.now().minusSeconds(1)))
        );

        assertThatThrownBy(() -> authService.refresh(new RefreshTokenUseCase.RefreshTokenCommand(refreshToken)))
                .isInstanceOf(DomainException.class)
                .extracting(ex -> ((DomainException) ex).getErrorCode())
                .isEqualTo(ErrorCode.REFRESH_TOKEN_EXPIRED);
    }

    @Test
    void signOut_removesSessionByRefreshToken() {
        String refreshToken = "refresh-to-remove";
        String hash = HashSupport.sha256(refreshToken);

        authService.signOut(new SignOutUseCase.SignOutCommand(refreshToken));

        verify(authSessionPort).deleteByRefreshTokenHash(hash);
    }

    @Test
    void socialLogin_createsIdentityAndReturnsTokens() {
        when(authJwtProperties.resolvedAccessTokenExpireSeconds()).thenReturn(900L);
        when(authJwtProperties.resolvedRefreshTokenExpireSeconds()).thenReturn(2592000L);
        when(authJwtTokenService.issueAccessToken(20L, "social@user.com", AuthProvider.GOOGLE, 202L))
                .thenReturn("google-access-jwt");
        when(authSocialTokenVerifier.verifyGoogle("google-id-token")).thenReturn(
                new AuthSocialProfile(AuthProvider.GOOGLE, "google-sub-1", "social@user.com", "Social User")
        );
        when(authIdentityPort.findByProviderAndProviderUserId(AuthProvider.GOOGLE, "google-sub-1")).thenReturn(Optional.empty());
        when(authUserPort.findByEmail("social@user.com")).thenReturn(Optional.empty());
        when(authUserPort.save(any(AuthUser.class))).thenReturn(AuthUser.restore(20L, "social@user.com", "social"));
        when(authIdentityPort.save(any(AuthIdentity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(authSessionPort.save(any(AuthSession.class))).thenReturn(
                AuthSession.restore(202L, 20L, AuthProvider.GOOGLE, "hash", LocalDateTime.now().plusDays(30))
        );

        VerifyMagicLinkUseCase.LoginResult result = authService.login(
                new SocialLoginUseCase.SocialLoginCommand(AuthProvider.GOOGLE, "google-id-token", null, "Social User")
        );

        assertThat(result.userId()).isEqualTo(20L);
        assertThat(result.email()).isEqualTo("social@user.com");
        assertThat(result.accessToken()).isEqualTo("google-access-jwt");
        assertThat(result.refreshToken()).isNotBlank();
        assertThat(result.provider()).isEqualTo(AuthProvider.GOOGLE);
    }

    @Test
    void getCurrentUser_returnsAuthenticatedUserView() {
        when(authUserPort.findById(77L)).thenReturn(Optional.of(AuthUser.restore(77L, "me@colly.app", "me")));

        GetCurrentUserUseCase.CurrentUserView result = authService.getCurrentUser(77L, AuthProvider.APPLE);

        assertThat(result.userId()).isEqualTo(77L);
        assertThat(result.email()).isEqualTo("me@colly.app");
        assertThat(result.provider()).isEqualTo(AuthProvider.APPLE);
    }
}
