package lab.coder.colly.domain.stay.adapter.out.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import lab.coder.colly.domain.community.adapter.out.persistence.entity.UserStayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserStayJpaRepository extends JpaRepository<UserStayEntity, Long> {

    List<UserStayEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("""
            select count(us) > 0
            from UserStayEntity us
            where us.userId = :userId
              and us.accommodationId = :accommodationId
              and us.checkIn = :checkIn
              and us.checkOut = :checkOut
            """)
    boolean existsDuplicate(
            @Param("userId") Long userId,
            @Param("accommodationId") Long accommodationId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

    @Query("""
            select count(us) > 0
            from UserStayEntity us
            where us.userId = :userId
              and us.accommodationId = :accommodationId
              and us.checkIn = :checkIn
              and us.checkOut = :checkOut
              and us.id <> :excludeId
            """)
    boolean existsDuplicateExcludingId(
            @Param("userId") Long userId,
            @Param("accommodationId") Long accommodationId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("excludeId") Long excludeId
    );

    List<UserStayEntity> findByAccommodationIdAndCheckInLessThanEqualAndCheckOutGreaterThanEqualAndUserIdNot(
            Long accommodationId,
            LocalDate checkOut,
            LocalDate checkIn,
            Long userId
    );
}
