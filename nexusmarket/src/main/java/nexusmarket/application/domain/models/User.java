package nexusmarket.application.domain.models;

import nexusmarket.application.domain.valueObjects.PersonState;
import nexusmarket.application.domain.valueObjects.RolesType;

@Getter
@Setter
@NoArgsConstructor
public class User extends Person {
    private final RolesType role;

    public User(
            String id,
            String fullName,
            String email,
            RolesType role) {

        super(
            id,
            fullName,
            email,
            PersonState.ACTIVE
        );

        if (role == null) {
            throw new IllegalArgumentException(
                "El rol es obligatorio"
            );
        }

        this.role = role;
    }

    public void inactivate() {
        if (getState() == PersonState.INACTIVE) {
            throw new IllegalStateException(
                "El usuario ya está bloqueado"
            );
        }

        changeState(PersonState.INACTIVE);
    }
}
