
package nexusmarket.application.domain.models;

import lombok.Getter;
import nexusmarket.application.domain.valueObjects.PersonState;

@Getter
public abstract class Person {

    private final String id;
    private String fullName;
    private String email;
    private PersonState state;

    protected Person(
            String id,
            String fullName,
            String email,
            PersonState state) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                "El identificador es obligatorio"
            );
        }

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException(
                "El nombre completo es obligatorio"
            );
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                "El correo electrónico es obligatorio"
            );
        }

        if (state == null) {
            throw new IllegalArgumentException(
                "El estado es obligatorio"
            );
        }

        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.state = state;
    }

    protected void changeState(PersonState state) {
        if (state == null) {
            throw new IllegalArgumentException(
                "El estado es obligatorio"
            );
        }

        this.state = state;
    }
}
