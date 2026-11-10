package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.exceptions.InvalidAddressException;

@Getter
@Setter
@NoArgsConstructor
public class Address {

    private String id;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String additionalInfo;

    public void validate() {
        requireNonBlank(street, "street");
        requireNonBlank(city, "city");
        requireNonBlank(country, "country");
    }

    public boolean isComplete() {
        return isNotBlank(street) && isNotBlank(city) && isNotBlank(country);
    }

    private void requireNonBlank(String value, String field) {
        if (isBlank(value)) {
            throw new InvalidAddressException("La direccion requiere el campo " + field + ".");
        }
    }

    private boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
