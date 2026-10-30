package nexusmarket.application.infrastructure.persistence;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import nexusmarket.application.domain.valueObjects.RolesType;

@Repository
@Profile("inmemory")
public class InMemoryUserRepository implements UserRepositoryPort {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    public InMemoryUserRepository() {
        users.put("admin", new User(
                "1",
                "Admin",
                "admin@nexusmarket.com",
                RolesType.ADMINISTRATOR,
                "admin",
                "123456"));
    }

    @Override
    public User save(User user) {
        users.put(user.getUsername(), user);
        return user;
    }

    @Override
    public Optional<User> findByUsername(User user) {
        return Optional.ofNullable(users.get(user.getUsername()));
    }

    @Override
    public Optional<User> findById(User user) {
        return Optional.ofNullable(users.values()
                .stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .orElse(null));
    }

    @Override
    public boolean existsByUsername(User user) {
        return users.containsKey(user.getUsername());
    }

    @Override
    public void update(User user) {
        users.put(user.getUsername(), user);
    }
}
