package lab.coder.colly.domain.stay.adapter.out.persistence.repository;

import java.util.List;
import lab.coder.colly.domain.community.adapter.out.persistence.entity.AccommodationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationJpaRepository extends JpaRepository<AccommodationEntity, Long> {

    List<AccommodationEntity> findByCountryCodeAndCityCodeOrderByNameAsc(String countryCode, String cityCode);
}
