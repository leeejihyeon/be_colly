package lab.coder.colly.domain.community.domain.model;

import java.time.LocalDateTime;

public class UserRestriction {

    private final Long id;
    private final Long userId;
    private final RestrictionType type;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final String reason;

    private UserRestriction(Long id, Long userId, RestrictionType type, LocalDateTime startAt, LocalDateTime endAt, String reason) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.startAt = startAt;
        this.endAt = endAt;
        this.reason = reason;
    }

    public static UserRestriction create(Long userId, RestrictionType type, LocalDateTime startAt, LocalDateTime endAt, String reason) {
        return new UserRestriction(null, userId, type, startAt, endAt, reason);
    }

    public static UserRestriction restore(Long id, Long userId, RestrictionType type, LocalDateTime startAt, LocalDateTime endAt, String reason) {
        return new UserRestriction(id, userId, type, startAt, endAt, reason);
    }

    public boolean isActiveAt(LocalDateTime now) {
        return !now.isBefore(startAt) && !now.isAfter(endAt);
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public RestrictionType getType() {
        return type;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public String getReason() {
        return reason;
    }
}
