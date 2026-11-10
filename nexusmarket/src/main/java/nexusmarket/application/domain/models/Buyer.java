package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.valueobjects.CommercialState;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Buyer extends Person {

    private Address primaryAddress;
    private List<Address> additionalAddresses = new ArrayList<>();
    private CommercialState commercialState = CommercialState.ACTIVE;

    public void addAdditionalAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("La direccion adicional es obligatoria.");
        }
        this.additionalAddresses.add(address);
    }

    public void activateCommercialState() {
        this.commercialState = CommercialState.ACTIVE;
    }

    public void inactivateCommercialState() {
        this.commercialState = CommercialState.INACTIVE;
    }

    public boolean canPurchase() {
        return CommercialState.ACTIVE.equals(this.commercialState) && isActive();
    }
}
