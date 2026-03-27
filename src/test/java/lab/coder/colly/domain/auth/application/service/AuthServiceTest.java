package lab.coder.colly.domain.auth.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import lab.coder.colly.domain.auth.application.port.in.IssueMagicLinkUseCase;
import lab.coder.colly.domain.auth.application.port.in.SocialLoginUseCase;
import lab.coder.colly.domain.auth.application.port.in.VerifyMagicLinkUseCase;
import lab.coder.colly.domain.auth.application.port.out.AuthIdentityPort;
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
    private AuthUserPort authUserPort;

    @Mock
    private AuthSessionPort authSessionPort;

    @Mock
    private AuthIdentityPort authIdentityPort;

    @InjectMocks
    private AuthService authService;

    @Test
    void issue_issuesMagicToken() {
        when(authMagicLinkPort.existsRecentByEmail(any(), any())).thenReturn(false);
        when(authMagicLinkPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        IssueMagicLinkUseCase.MagicLinkIssueResult result = authService.issue(
            new IssueMagicLinkUseCase.MagicLinkIssueCommand("hello@example.com")
        );

        assertThat(result.email()).isEqualTo("hello@example.com");
        assertThat(result.magicToken()).isNotBlank();
    }

    @Test
    void verify_rejectsWhenMagicLinkIsExpired() {
        String token = "token-value";
        when(authMagicLinkPort.findByTokenHash(HashSupport.sha256(token))).thenReturn(Optional.of(
            AuthMagicLink.restore(1L, "a@b.com", HashSupport.sha256(token), LocalDateTime.now().minusSeconds(1), null)
        ));

        assertThatThrownBy(() -> authService.verify(new VerifyMagicLinkUseCase.VerifyMagicLinkCommand(token)))
            .isInstanceOf(DomainException.class);
    }

    @Test
    void verify_createsSessionAndReturnsTokens() {
        String token = "token-value";
        String tokenHash = HashSupport.sha256(token);

        when(authMagicLinkPort.findByTokenHash(tokenHash)).thenReturn(Optional.of(
            AuthMagicLink.restore(1L, "new@user.com", tokenHash, LocalDateTime.now().plusMinutes(5), null)
        ));
        when(authMagicLinkPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(authUserPort.findByEmail("new@user.com")).thenReturn(Optional.of(AuthUser.restore(10L, "new@user.com", "new")));
        when(authSessionPort.save(any(AuthSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VerifyMagicLinkUseCase.LoginResult result = authService.verify(new VerifyMagicLinkUseCase.VerifyMagicLinkCommand(token));

        assertThat(result.userId()).isEqualTo(10L);
        assertThat(result.accessToken()).isNotBlank();
        assertThat(result.refreshToken()).isNotBlank();
    }

    @Test
    void socialLogin_createsIdentityAndReturnsTokens() {
        when(authIdentityPort.findByProviderAndProviderUserId(AuthProvider.GOOGLE, "google-sub-1")).thenReturn(Optional.empty());
        when(authUserPort.findByEmail("social@user.com")).thenReturn(Optional.empty());
        when(authUserPort.save(any(AuthUser.class))).thenReturn(AuthUser.restore(20L, "social@user.com", "social"));
        when(authIdentityPort.save(any(AuthIdentity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(authSessionPort.save(any(AuthSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SocialLoginUseCase.SocialLoginResult result = authService.login(
            new SocialLoginUseCase.SocialLoginCommand(AuthProvider.GOOGLE, "google-sub-1", "social@user.com", "Social User")
        );

        assertThat(result.userId()).isEqualTo(20L);
        assertThat(result.email()).isEqualTo("social@user.com");
        assertThat(result.accessToken()).isNotBlank();
        assertThat(result.refreshToken()).isNotBlank();
    }
}
