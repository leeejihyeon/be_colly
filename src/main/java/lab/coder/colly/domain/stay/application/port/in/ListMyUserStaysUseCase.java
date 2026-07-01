package lab.coder.colly.domain.stay.application.port.in;

import java.util.List;

public interface ListMyUserStaysUseCase {

    List<UserStayView> listMyStays(Long userId);
}
