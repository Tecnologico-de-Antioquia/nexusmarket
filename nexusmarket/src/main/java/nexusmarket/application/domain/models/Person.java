package nexusmarket.application.domain.models;

import nexusmarket.application.domain.valueObjects.PersonState;

public abstract class Person {
    private String id;
    private String fullName;
    private String email;
    private PersonState state;
}
