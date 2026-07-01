package lab.coder.colly.domain.stay.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserStay {

    private final Long id;
    private final Long userId;
    private final Long accommodationId;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final LocalDateTime createdAt;

    private UserStay(
            Long id,
            Long userId,
            Long accommodationId,
            LocalDate checkIn,
            LocalDate checkOut,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.accommodationId = accommodationId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.createdAt = createdAt;
    }

    public static UserStay create(
            Long userId,
            Long accommodationId,
            LocalDate checkIn,
            LocalDate checkOut
    ) {
        return new UserStay(null, userId, accommodationId, checkIn, checkOut, null);
    }

    public static UserStay restore(
            Long id,
            Long userId,
            Long accommodationId,
            LocalDate checkIn,
            LocalDate checkOut,
            LocalDateTime createdAt
    ) {
        return new UserStay(id, userId, accommodationId, checkIn, checkOut, createdAt);
    }

    public UserStay change(
            Long accommodationId,
            LocalDate checkIn,
            LocalDate checkOut
    ) {
        return new UserStay(id, userId, accommodationId, checkIn, checkOut, createdAt);
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
