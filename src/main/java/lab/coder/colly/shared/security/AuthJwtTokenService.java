package lab.coder.colly.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import lab.coder.colly.domain.auth.domain.model.AuthProvider;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class AuthJwtTokenService {

    private final AuthJwtProperties authJwtProperties;
    private SecretKey secretKey;

    public AuthJwtTokenService(AuthJwtProperties authJwtProperties) {
        this.authJwtProperties = authJwtProperties;
    }

    @PostConstruct
    void init() {
        if (!authJwtProperties.hasSecret()) {
            throw new IllegalStateException(
                    "colly.auth.jwt.secret must be configured and contain at least 32 bytes"
            );
        }
        byte[] bytes = authJwtProperties.resolvedSecret().getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException(
                    "colly.auth.jwt.secret must be at least 32 bytes"
            );
        }
        this.secretKey = Keys.hmacShaKeyFor(bytes);
    }

    public String issueAccessToken(
            Long userId,
            String email,
            AuthProvider provider,
            Long sessionId
    ) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(authJwtProperties.resolvedAccessTokenExpireSeconds());

        return Jwts.builder()
                .issuer(authJwtProperties.resolvedIssuer())
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("provider", provider.name())
                .claim("sessionId", sessionId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(secretKey)
                .compact();
    }

    public AuthenticatedUser parseAuthenticatedUser(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .requireIssuer(authJwtProperties.resolvedIssuer())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Long userId = Long.valueOf(claims.getSubject());
            String email = claims.get("email", String.class);
            String provider = claims.get("provider", String.class);
            Long sessionId = claims.get("sessionId", Long.class);

            return new AuthenticatedUser(
                    userId,
                    email,
                    AuthProvider.valueOf(provider),
                    sessionId
            );
        } catch (Exception ex) {
            throw new DomainException(
                    ErrorCode.AUTH_INVALID_ACCESS_TOKEN,
                    "Access token is invalid or expired"
            );
        }
    }
}
