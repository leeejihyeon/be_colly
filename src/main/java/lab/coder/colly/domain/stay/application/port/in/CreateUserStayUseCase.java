package lab.coder.colly.domain.stay.application.port.in;

import java.time.LocalDate;

public interface CreateUserStayUseCase {

    UserStayView create(CreateUserStayCommand command);

    record CreateUserStayCommand(
            Long userId,
            Long accommodationId,
            LocalDate checkIn,
            LocalDate checkOut
    ) {
    }
}
