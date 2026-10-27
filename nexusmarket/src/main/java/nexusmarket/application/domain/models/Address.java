package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
    private String id;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String additionalInfo;

    public Address(String street, String city, String state, String postalCode, String country) {
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("Street is required");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City is required");
        }
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException("State is required");
        }
        if (postalCode == null || postalCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Postal code is required");
        }
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Country is required");
        }

        this.street = street.trim();
        this.city = city.trim();
        this.state = state.trim();
        this.postalCode = postalCode.trim();
        this.country = country.trim();
    }
}
