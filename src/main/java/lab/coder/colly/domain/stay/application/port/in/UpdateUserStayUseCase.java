package lab.coder.colly.domain.stay.application.port.in;

import java.time.LocalDate;

public interface UpdateUserStayUseCase {

    UserStayView update(UpdateUserStayCommand command);

    record UpdateUserStayCommand(
            Long stayId,
            Long userId,
            Long accommodationId,
            LocalDate checkIn,
            LocalDate checkOut
    ) {
    }
}
