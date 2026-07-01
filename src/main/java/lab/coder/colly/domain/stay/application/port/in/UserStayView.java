package lab.coder.colly.domain.stay.application.port.in;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserStayView(
        Long id,
        Long accommodationId,
        LocalDate checkIn,
        LocalDate checkOut,
        LocalDateTime createdAt
) {
}
