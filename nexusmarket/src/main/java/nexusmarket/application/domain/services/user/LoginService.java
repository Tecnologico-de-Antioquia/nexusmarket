package nexusmarket.application.domain.services.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.DomainException;
import nexusmarket.application.domain.exceptions.InvalidCredentialsException;
import nexusmarket.application.domain.models.AuthenticationResult;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.PasswordServicePort;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import nexusmarket.application.domain.valueObjects.PersonState;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordServicePort passwordServicePort;

    public AuthenticationResult login(User user) {
        if (user == null) {
            throw new InvalidCredentialsException();
        }

        Optional<User> storedOpt = userRepositoryPort.findByUsername(user);
        if (storedOpt.isEmpty()) {
            throw new InvalidCredentialsException();
        }

        User stored = storedOpt.get();
        if (!passwordServicePort.matches(user.getPassword(), stored.getPassword())) {
            throw new InvalidCredentialsException();
        }

        validateUserStatus(stored);
        return new AuthenticationResult(stored, "token-" + stored.getId());
    }

    private void validateUserStatus(User user) {
        if (user.getPersonState() == null || user.getPersonState().equals(PersonState.INACTIVE)) {
            throw new DomainException("User account is inactive.");
        }
    }
}