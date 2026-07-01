package lab.coder.colly.domain.stay.application.port.in;

public interface DeleteUserStayUseCase {

    void delete(DeleteUserStayCommand command);

    record DeleteUserStayCommand(
            Long stayId,
            Long userId
    ) {
    }
}
