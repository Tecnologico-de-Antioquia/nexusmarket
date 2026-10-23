package nexusmarket.application.domain.models;

import lombok.Getter;
import nexusmarket.application.domain.valueObjects.ComercialState;
import nexusmarket.application.domain.valueObjects.PersonState;

@Getter
public class Buyer extends Person {
    private Address primaryAddres;
    private Address additionalAddress;
    private ComercialState comercialState;

    public Buyer(
            String id,
            String fullName,
            String email) {

        super(id, fullName, email, PersonState.ACTIVE);
    }

    public void inactivate(){
        if(getComercialState() == ComercialState.INACTIVE){
            throw new IllegalStateException("El estado comercial ya está inactivo");
        }
    }
}
