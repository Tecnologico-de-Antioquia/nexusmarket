package nexusmarket.application.domain.ports.out;

import nexusmarket.application.domain.models.User;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);

    Optional<User> findByUsername(User user);

    Optional<User> findById(User user);

    boolean existsByUsername(User user);

    void update(User user);
}
