package lab.coder.colly.domain.auth.domain.model;

import java.time.LocalDateTime;

public class AuthMagicLink {

    private final Long id;
    private final String email;
    private final String tokenHash;
    private final LocalDateTime expiresAt;
    private final LocalDateTime usedAt;

    private AuthMagicLink(Long id, String email, String tokenHash, LocalDateTime expiresAt, LocalDateTime usedAt) {
        this.id = id;
        this.email = email;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.usedAt = usedAt;
    }

    public static AuthMagicLink issue(String email, String tokenHash, LocalDateTime expiresAt) {
        return new AuthMagicLink(null, email, tokenHash, expiresAt, null);
    }

    public static AuthMagicLink restore(Long id, String email, String tokenHash, LocalDateTime expiresAt, LocalDateTime usedAt) {
        return new AuthMagicLink(id, email, tokenHash, expiresAt, usedAt);
    }

    public AuthMagicLink markUsed(LocalDateTime usedAt) {
        return new AuthMagicLink(id, email, tokenHash, expiresAt, usedAt);
    }

    public boolean isExpiredAt(LocalDateTime now) {
        return now.isAfter(expiresAt);
    }

    public boolean isUsed() {
        return usedAt != null;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }
}
