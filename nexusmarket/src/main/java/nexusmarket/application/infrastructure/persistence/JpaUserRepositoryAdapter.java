package nexusmarket.application.infrastructure.persistence;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import nexusmarket.application.domain.valueObjects.PersonState;

@Repository
@Primary
@Profile("!inmemory")
public class JpaUserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    public JpaUserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        return toDomain(userJpaRepository.save(toEntity(user)));
    }

    @Override
    public Optional<User> findByUsername(User user) {
        return userJpaRepository.findByUsername(user.getUsername()).map(this::toDomain);
    }

    @Override
    public Optional<User> findById(User user) {
        return userJpaRepository.findById(user.getId()).map(this::toDomain);
    }

    @Override
    public boolean existsByUsername(User user) {
        return userJpaRepository.existsByUsername(user.getUsername());
    }

    @Override
    public void update(User user) {
        userJpaRepository.save(toEntity(user));
    }

    private UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        return new UserEntity(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPersonState() != null ? user.getPersonState() : PersonState.ACTIVE,
                user.getRole(),
                user.getUsername(),
                user.getPassword()
        );
    }

    private User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return new User(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getRole(),
                entity.getUsername(),
                entity.getPassword()
        );
    }
}
