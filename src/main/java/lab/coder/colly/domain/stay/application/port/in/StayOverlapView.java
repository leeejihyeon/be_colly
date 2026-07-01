package lab.coder.colly.domain.stay.application.port.in;

import java.time.LocalDate;

public record StayOverlapView(
        Long userId,
        String nickname,
        LocalDate overlapStartDate,
        LocalDate overlapEndDate
) {
}
