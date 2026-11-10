package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationResult {

    private User authenticatedUser;
    private String token;
    private long expiresIn;

    public AuthenticationResult(User authenticatedUser, String token, long expiresIn) {
        this.authenticatedUser = authenticatedUser;
        this.token = token;
        this.expiresIn = expiresIn;
    }
}
