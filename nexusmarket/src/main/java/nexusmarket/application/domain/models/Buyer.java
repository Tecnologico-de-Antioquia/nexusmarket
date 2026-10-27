package nexusmarket.application.domain.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import nexusmarket.application.domain.valueObjects.ComercialState;
import nexusmarket.application.domain.valueObjects.PersonState;

@Getter
@Setter
public class Buyer extends Person {
    private Address primaryAddress;
    private List<Address> additionalAddresses = new ArrayList<>();
    private ComercialState comercialState;

    public Buyer(String id, String fullName, String email) {
        super(id, fullName, email, PersonState.ACTIVE);
        this.comercialState = ComercialState.ACTIVE;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        if (primaryAddress == null) {
            throw new IllegalArgumentException("Primary address is required");
        }
        this.primaryAddress = primaryAddress;
    }

    public void addAdditionalAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address is required");
        }
        this.additionalAddresses.add(address);
    }

    public void activateCommercialState() {
        if (this.comercialState == ComercialState.ACTIVE) {
            throw new IllegalStateException("Buyer is already active for commercial operations");
        }
        this.comercialState = ComercialState.ACTIVE;
    }

    public void inactivate() {
        if (this.comercialState == ComercialState.INACTIVE) {
            throw new IllegalStateException("Buyer commercial state is already inactive");
        }
        this.comercialState = ComercialState.INACTIVE;
    }
}
