package lab.coder.colly.domain.community.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lab.coder.colly.shared.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_stays")
public class UserStayEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long accommodationId;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Column(length = 120)
    private String roomType;

    public UserStayEntity(
            Long id,
            Long userId,
            Long accommodationId,
            LocalDate checkIn,
            LocalDate checkOut,
            String roomType
    ) {
        this.id = id;
        this.userId = userId;
        this.accommodationId = accommodationId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.roomType = roomType;
    }
}
