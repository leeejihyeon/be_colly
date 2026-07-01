package lab.coder.colly.domain.stay.application.port.in;

import java.util.List;

public interface ListAccommodationsUseCase {

    List<AccommodationView> list(ListAccommodationsQuery query);

    record ListAccommodationsQuery(
            String countryCode,
            String cityCode
    ) {
    }
}
