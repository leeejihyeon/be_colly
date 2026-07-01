package lab.coder.colly.domain.stay.application.port.out;

import java.util.List;
import java.util.Optional;
import lab.coder.colly.domain.stay.domain.model.Accommodation;

public interface AccommodationPort {

    List<Accommodation> findByCountryCodeAndCityCode(String countryCode, String cityCode);

    Optional<Accommodation> findAccommodationById(Long accommodationId);
}
