package lab.coder.colly.shared.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "colly.auth.social")
public record AuthSocialProperties(
        Provider google,
        Provider apple
) {

    public record Provider(
            String issuer,
            String jwkSetUri,
            String audience
    ) {
    }
}
