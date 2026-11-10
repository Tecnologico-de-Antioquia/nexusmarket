package nexusmarket.application.domain.services.user;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadAuthenticatedUserService {

    private final UserRepositoryPort userRepositoryPort;

    public User loadById(String id) {
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));
    }

    public User loadByUsername(String username) {
        return userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));
    }
}
