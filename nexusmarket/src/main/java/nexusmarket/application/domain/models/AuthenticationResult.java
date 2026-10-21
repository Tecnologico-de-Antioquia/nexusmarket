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

    public AuthenticationResult(User authenticatedUser, String token) {
        this.authenticatedUser = authenticatedUser;
        this.token = token;
    }
}
