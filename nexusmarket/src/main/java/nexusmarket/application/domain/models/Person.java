package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.valueobjects.SystemRole;

@Getter
@Setter
@NoArgsConstructor
public abstract class Person {

    private String id;
    private String fullName;
    private String email;

    public void activate() {
        // state moved to User; no-op here
    }

    public void block() {
        // state moved to User; no-op here
    }

    public void inactivate() {
        // state moved to User; no-op here
    }

    public boolean isActive() {
        return false; // state in User
    }
}