package nexusmarket.application.adapters.persistence.jpa.adapters;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.persistence.jpa.entities.UserEntity;
import nexusmarket.application.adapters.persistence.jpa.mappers.JpaMapper;
import nexusmarket.application.adapters.persistence.jpa.repositories.JpaUserRepository;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaUserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository repository;

    @Override
    public User save(User user) {
        UserEntity entity = repository.save(JpaMapper.toEntity(user));
        user.setId(entity.getId());
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id).map(JpaMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(JpaMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(JpaMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(JpaMapper::toDomain).toList();
    }

    @Override
    public void update(User user) {
        repository.save(JpaMapper.toEntity(user));
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}
