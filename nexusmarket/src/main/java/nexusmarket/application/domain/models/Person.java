package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.valueobjects.SystemRole;
import nexusmarket.application.domain.valueobjects.UserStatus;

@Getter
@Setter
@NoArgsConstructor
public abstract class Person {

    private String id;
    private String fullName;
    private String email;
    private String username;
    private String password;
    private SystemRole role;
    private UserStatus state;

    public void activate() {
        this.state = UserStatus.ACTIVE;
    }

    public void block() {
        this.state = UserStatus.BLOCKED;
    }

    public void inactivate() {
        this.state = UserStatus.INACTIVE;
    }

    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.state);
    }
}
