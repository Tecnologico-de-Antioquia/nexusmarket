
package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.Setter;
import nexusmarket.application.domain.valueObjects.PersonState;

@Getter
@Setter
public abstract class Person {

    private final String id;
    private String fullName;
    private String email;
    private PersonState personState;

    protected Person(
            String id,
            String fullName,
            String email,
            PersonState personState) {

        if (fullName == null || fullName.trim().equals("")) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (email == null || email.trim().equals("")) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.personState = personState;
    }

}
