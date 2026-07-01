package lab.coder.colly.domain.stay.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UpsertUserStayRequest(
        @NotNull Long accommodationId,
        @NotNull LocalDate checkIn,
        @NotNull LocalDate checkOut
) {
}
