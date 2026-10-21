package nexusmarket.application.domain.services.user;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.AuthenticationResult;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.PasswordServicePort;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import nexusmarket.application.domain.exceptions.DomainException;
import nexusmarket.application.domain.exceptions.InvalidCredentialsException;
import java.util.Optional;
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

        return new AuthenticationResult(storedOpt, "");
    }

    private void validateUserStatus(User stored) {
        if (UserStatus.BLOCKED.equals(stored.getStatus())) {
            throw new DomainException("User account is blocked.");
        }
        if (UserStatus.INACTIVE.equals(stored.getStatus())) {
            throw new DomainException("User account is inactive.");
        }
    }
}