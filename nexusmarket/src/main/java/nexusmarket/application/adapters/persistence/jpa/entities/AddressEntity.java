package nexusmarket.application.adapters.persistence.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
public class AddressEntity {

    @Id
    private String id;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String additionalInfo;
}
