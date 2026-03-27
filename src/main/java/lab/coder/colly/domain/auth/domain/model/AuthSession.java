package lab.coder.colly.domain.auth.domain.model;

import java.time.LocalDateTime;

public class AuthSession {

    private final Long id;
    private final Long userId;
    private final String refreshTokenHash;
    private final LocalDateTime expiresAt;

    private AuthSession(Long id, Long userId, String refreshTokenHash, LocalDateTime expiresAt) {
        this.id = id;
        this.userId = userId;
        this.refreshTokenHash = refreshTokenHash;
        this.expiresAt = expiresAt;
    }

    public static AuthSession create(Long userId, String refreshTokenHash, LocalDateTime expiresAt) {
        return new AuthSession(null, userId, refreshTokenHash, expiresAt);
    }

    public static AuthSession restore(Long id, Long userId, String refreshTokenHash, LocalDateTime expiresAt) {
        return new AuthSession(id, userId, refreshTokenHash, expiresAt);
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRefreshTokenHash() {
        return refreshTokenHash;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
