package lab.coder.colly.domain.community.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lab.coder.colly.shared.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "accommodations")
public class AccommodationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2)
    private String countryCode;

    @Column(nullable = false, length = 50)
    private String cityCode;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 255)
    private String addressLine1;

    @Column(length = 255)
    private String addressLine2;

    public AccommodationEntity(
            Long id,
            String countryCode,
            String cityCode,
            String name,
            String addressLine1,
            String addressLine2
    ) {
        this.id = id;
        this.countryCode = countryCode;
        this.cityCode = cityCode;
        this.name = name;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
    }
}
