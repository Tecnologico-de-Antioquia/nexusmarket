package nexusmarket.application.domain.ports.out;

import nexusmarket.application.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findAll();

    void update(User user);

    void delete(String id);
}
