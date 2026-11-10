package nexusmarket.application.domain.services.user;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.InvalidCredentialsException;
import nexusmarket.application.domain.exceptions.UnauthorizedOperationException;
import nexusmarket.application.domain.models.AuthenticationResult;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.JwtServicePort;
import nexusmarket.application.domain.ports.out.PasswordServicePort;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordServicePort passwordServicePort;
    private final JwtServicePort jwtServicePort;

    public AuthenticationResult login(User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            throw new InvalidCredentialsException();
        }
        User stored = userRepositoryPort.findByUsername(user.getUsername())
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordServicePort.matches(user.getPassword(), stored.getPassword())) {
            throw new InvalidCredentialsException();
        }
        if (!stored.isActive()) {
            throw new UnauthorizedOperationException("El usuario no se encuentra activo.");
        }
        String token = jwtServicePort.generateToken(stored);
        return new AuthenticationResult(stored, token, jwtServicePort.getExpirationMs() / 1000);
    }
}
