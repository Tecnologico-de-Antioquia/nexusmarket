package nexusmarket.application.domain.services.security;

import nexusmarket.application.domain.exceptions.UnauthorizedOperationException;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    public User requireAuthenticated(User user) {
        if (user == null || user.getId() == null || user.getId().isBlank()) {
            throw new UnauthorizedOperationException("Se requiere un usuario autenticado.");
        }
        return user;
    }

    public User requireActive(User user) {
        requireAuthenticated(user);
        if (!user.isActive()) {
            throw new UnauthorizedOperationException("El usuario no se encuentra activo.");
        }
        return user;
    }

    public User requireRole(User user, SystemRole role) {
        requireAuthenticated(user);
        if (role == null || !role.equals(user.getRole())) {
            throw new UnauthorizedOperationException("El usuario no tiene el rol requerido.");
        }
        return user;
    }

    public User requireActiveRole(User user, SystemRole role) {
        requireRole(user, role);
        requireActive(user);
        return user;
    }

    public User requireAnyRole(User user, SystemRole... roles) {
        requireAuthenticated(user);
        for (SystemRole role : roles) {
            if (role.equals(user.getRole())) {
                return user;
            }
        }
        throw new UnauthorizedOperationException("El usuario no tiene permiso para ejecutar la operacion.");
    }
}
