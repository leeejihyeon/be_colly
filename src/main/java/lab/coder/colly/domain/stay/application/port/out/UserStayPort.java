package lab.coder.colly.domain.stay.application.port.out;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lab.coder.colly.domain.stay.domain.model.UserStay;

public interface UserStayPort {

    UserStay save(UserStay userStay);

    Optional<UserStay> findById(Long stayId);

    List<UserStay> findByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsDuplicate(Long userId, Long accommodationId, LocalDate checkIn, LocalDate checkOut, Long excludeStayId);

    void delete(UserStay userStay);

    List<UserStay> findOverlaps(Long accommodationId, LocalDate checkIn, LocalDate checkOut, Long excludeUserId);
}
