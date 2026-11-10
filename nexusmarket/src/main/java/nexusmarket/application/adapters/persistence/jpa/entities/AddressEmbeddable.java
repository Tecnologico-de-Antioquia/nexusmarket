package nexusmarket.application.adapters.persistence.jpa.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class AddressEmbeddable {

    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String additionalInfo;
}
