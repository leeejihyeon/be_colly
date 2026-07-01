package lab.coder.colly.domain.stay.domain.model;

public class Accommodation {

    private final Long id;
    private final String countryCode;
    private final String cityCode;
    private final String name;
    private final String addressLine1;
    private final String addressLine2;

    private Accommodation(
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

    public static Accommodation restore(
            Long id,
            String countryCode,
            String cityCode,
            String name,
            String addressLine1,
            String addressLine2
    ) {
        return new Accommodation(id, countryCode, cityCode, name, addressLine1, addressLine2);
    }

    public Long getId() {
        return id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getName() {
        return name;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }
}
