package lab.coder.colly.shared.security;

import java.util.List;
import lab.coder.colly.domain.auth.domain.model.AuthProvider;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class AuthSocialTokenVerifier {

    private final JwtDecoder googleJwtDecoder;
    private final JwtDecoder appleJwtDecoder;

    public AuthSocialTokenVerifier(AuthSocialProperties authSocialProperties) {
        this.googleJwtDecoder = buildDecoder(authSocialProperties.google(), List.of(
                "accounts.google.com",
                "https://accounts.google.com"
        ));
        this.appleJwtDecoder = buildDecoder(authSocialProperties.apple(), List.of(
                "https://appleid.apple.com"
        ));
    }

    public AuthSocialProfile verifyGoogle(String idToken) {
        Jwt jwt = decode(googleJwtDecoder, idToken);

        return new AuthSocialProfile(
                AuthProvider.GOOGLE,
                jwt.getSubject(),
                jwt.getClaimAsString("email"),
                jwt.getClaimAsString("name")
        );
    }

    public AuthSocialProfile verifyApple(String identityToken, String name) {
        Jwt jwt = decode(appleJwtDecoder, identityToken);
        String email = jwt.getClaimAsString("email");
        String fallbackEmail = "apple-" + jwt.getSubject() + "@users.colly.local";

        return new AuthSocialProfile(
                AuthProvider.APPLE,
                jwt.getSubject(),
                email == null || email.isBlank() ? fallbackEmail : email,
                name
        );
    }

    private Jwt decode(JwtDecoder jwtDecoder, String token) {
        if (token == null || token.isBlank()) {
            throw new DomainException(
                    ErrorCode.AUTH_SOCIAL_TOKEN_INVALID,
                    "Provider token is required"
            );
        }

        try {
            return jwtDecoder.decode(token);
        } catch (Exception ex) {
            throw new DomainException(
                    ErrorCode.AUTH_SOCIAL_TOKEN_INVALID,
                    "Provider token is invalid"
            );
        }
    }

    private JwtDecoder buildDecoder(AuthSocialProperties.Provider provider, List<String> allowedIssuers) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(provider.jwkSetUri()).build();
        OAuth2TokenValidator<Jwt> withIssuer = jwt -> {
            String issuer = jwt.getIssuer() == null ? null : jwt.getIssuer().toString();
            if (issuer != null && allowedIssuers.contains(issuer)) {
                return OAuth2TokenValidatorResult.success();
            }
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Invalid issuer", null));
        };
        OAuth2TokenValidator<Jwt> withAudience = new JwtClaimValidator<List<String>>(
                "aud",
                audience -> audience != null && audience.contains(provider.audience())
        );
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidatorCompat(
                JwtValidators.createDefault(),
                withIssuer,
                withAudience
        ));
        return decoder;
    }

    private static final class DelegatingOAuth2TokenValidatorCompat implements OAuth2TokenValidator<Jwt> {

        private final List<OAuth2TokenValidator<Jwt>> validators;

        @SafeVarargs
        private DelegatingOAuth2TokenValidatorCompat(OAuth2TokenValidator<Jwt>... validators) {
            this.validators = List.of(validators);
        }

        @Override
        public OAuth2TokenValidatorResult validate(Jwt token) {
            for (OAuth2TokenValidator<Jwt> validator : validators) {
                OAuth2TokenValidatorResult result = validator.validate(token);
                if (result.hasErrors()) {
                    return result;
                }
            }
            return OAuth2TokenValidatorResult.success();
        }
    }
}
