package lab.coder.colly.domain.stay.adapter.out.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import lab.coder.colly.domain.community.adapter.out.persistence.entity.UserStayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStayJpaRepository extends JpaRepository<UserStayEntity, Long> {

    List<UserStayEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByUserIdAndAccommodationIdAndCheckInAndCheckOut(Long userId, Long accommodationId, LocalDate checkIn, LocalDate checkOut);

    boolean existsByUserIdAndAccommodationIdAndCheckInAndCheckOutAndIdNot(
            Long userId,
            Long accommodationId,
            LocalDate checkIn,
            LocalDate checkOut,
            Long id
    );

    List<UserStayEntity> findByAccommodationIdAndCheckInLessThanEqualAndCheckOutGreaterThanEqualAndUserIdNot(
            Long accommodationId,
            LocalDate checkOut,
            LocalDate checkIn,
            Long userId
    );
}
