package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
