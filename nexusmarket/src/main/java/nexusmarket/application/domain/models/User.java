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
        super(id, fullName, email, PersonState.ACTIVE);

        if (role == null) {
            throw new IllegalArgumentException("Role is required");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        this.role = role;
        this.username = username.trim();
        this.password = password;
    }

    public void activate() {
        if (getPersonState() == PersonState.ACTIVE) {
            throw new IllegalStateException("User is already active");
        }
        setPersonState(PersonState.ACTIVE);
    }

    public void inactivate() {
        if (getPersonState() == PersonState.INACTIVE) {
            throw new IllegalStateException("User is already inactive");
        }
        setPersonState(PersonState.INACTIVE);
    }
}
