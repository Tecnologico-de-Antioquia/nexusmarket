package nexusmarket.application.domain.services.user;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManageUserService {

    private final UserRepositoryPort userRepositoryPort;
    private final AuthorizationService authorizationService;

    public List<User> consultAll(User admin) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        return userRepositoryPort.findAll();
    }

    public User consultById(User admin, String userId) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        return userRepositoryPort.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));
    }

    public User update(User admin, User user) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        User stored = userRepositoryPort.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));
        if (user.getFullName() != null) {
            stored.setFullName(user.getFullName());
        }
        if (user.getEmail() != null) {
            stored.setEmail(user.getEmail());
        }
        if (user.getRole() != null) {
            stored.setRole(user.getRole());
        }
        return userRepositoryPort.save(stored);
    }

    public User block(User admin, String userId) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        User stored = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));
        stored.block();
        return userRepositoryPort.save(stored);
    }

    public User activate(User admin, String userId) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        User stored = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));
        stored.activate();
        return userRepositoryPort.save(stored);
    }
}
