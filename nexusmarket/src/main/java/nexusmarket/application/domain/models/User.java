package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User extends Person {

    private Integer authTokenVersion = 1;

    public void bumpAuthTokenVersion() {
        int current = authTokenVersion == null ? 0 : authTokenVersion;
        this.authTokenVersion = current + 1;
    }
}
