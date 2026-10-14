
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

        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.state = state;
    }

}
