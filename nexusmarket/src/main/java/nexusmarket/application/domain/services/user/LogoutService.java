package nexusmarket.application.domain.services.user;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final UserRepositoryPort userRepositoryPort;

    public void logout(User user) {
        if (user == null || user.getId() == null) {
            return;
        }
        userRepositoryPort.findById(user.getId()).ifPresent(stored -> {
            stored.bumpAuthTokenVersion();
            userRepositoryPort.update(stored);
        });
    }
}
