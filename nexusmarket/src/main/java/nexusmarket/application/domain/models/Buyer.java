package nexusmarket.application.domain.models;

import nexusmarket.application.domain.valueObjects.ComercialState;

public class Buyer extends Person {
    private Address primaryAddres;
    private Address additionalAddress;
    private ComercialState state;
}
