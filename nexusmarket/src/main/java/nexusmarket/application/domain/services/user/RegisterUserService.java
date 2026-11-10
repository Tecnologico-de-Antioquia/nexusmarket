package nexusmarket.application.domain.services.user;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.InvalidRequestException;
import nexusmarket.application.domain.exceptions.ResourceAlreadyExistsException;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.PasswordServicePort;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import nexusmarket.application.domain.valueobjects.SystemRole;
import nexusmarket.application.domain.valueobjects.UserStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterUserService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordServicePort passwordServicePort;

    public User register(User newUser) {
        if (newUser == null || newUser.getUsername() == null || newUser.getUsername().isBlank()) {
            throw new InvalidRequestException("El nombre de usuario es obligatorio.");
        }
        if (newUser.getPassword() == null || newUser.getPassword().isBlank()) {
            throw new InvalidRequestException("La contrasena es obligatoria.");
        }
        if (userRepositoryPort.existsByUsername(newUser.getUsername())) {
            throw new ResourceAlreadyExistsException("El nombre de usuario ya existe.");
        }
        if (newUser.getEmail() != null && userRepositoryPort.existsByEmail(newUser.getEmail())) {
            throw new ResourceAlreadyExistsException("El correo electronico ya existe.");
        }
        newUser.setId(UUID.randomUUID().toString());
        newUser.setPassword(passwordServicePort.encrypt(newUser.getPassword()));
        newUser.setState(UserStatus.ACTIVE);
        if (newUser.getRole() == null) {
            newUser.setRole(SystemRole.BUYER);
        }
        return userRepositoryPort.save(newUser);
    }
}
