package lab.coder.colly.shared.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "colly.auth.jwt")
public record AuthJwtProperties(
        String issuer,
        String secret,
        long accessTokenExpireSeconds,
        long refreshTokenExpireSeconds
) {

    public String resolvedIssuer() {
        return issuer == null || issuer.isBlank() ? "colly-api" : issuer.trim();
    }

    public String resolvedSecret() {
        return secret == null ? "" : secret.trim();
    }

    public boolean hasSecret() {
        return !resolvedSecret().isBlank();
    }

    public long resolvedAccessTokenExpireSeconds() {
        return accessTokenExpireSeconds <= 0 ? 900 : accessTokenExpireSeconds;
    }

    public long resolvedRefreshTokenExpireSeconds() {
        return refreshTokenExpireSeconds <= 0 ? 30L * 24 * 60 * 60 : refreshTokenExpireSeconds;
    }
}
