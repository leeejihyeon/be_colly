package lab.coder.colly.domain.stay.application.port.in;

public record AccommodationView(
        Long id,
        String countryCode,
        String cityCode,
        String name,
        String addressLine1,
        String addressLine2
) {
}
