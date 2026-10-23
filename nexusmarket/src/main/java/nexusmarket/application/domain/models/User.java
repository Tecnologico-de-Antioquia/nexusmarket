package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.Setter;

import nexusmarket.application.domain.valueObjects.PersonState;
import nexusmarket.application.domain.valueObjects.RolesType;

@Getter
@Setter
public class User extends Person {

    private String username;
    private String password;
    private final RolesType role;

    public User(
            String id,
            String fullName,
            String email,
            RolesType role,
            String username,
            String password
        ) {

        super(
                id,
                fullName,
                email,
                PersonState.ACTIVE);

        if (role == null) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        this.role = role;
        this.username = username;
        this.password = password;
    }

    public void inactivate() {
        if (getPersonState() == PersonState.ACTIVE) {
            throw new IllegalStateException("El usuario ya está bloqueado");
        }

        setPersonState(PersonState.INACTIVE);
    }
}
