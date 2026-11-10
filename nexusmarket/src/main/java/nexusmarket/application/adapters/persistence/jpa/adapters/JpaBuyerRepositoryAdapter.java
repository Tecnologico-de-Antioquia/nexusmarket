package nexusmarket.application.adapters.persistence.jpa.adapters;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.persistence.jpa.entities.BuyerEntity;
import nexusmarket.application.adapters.persistence.jpa.mappers.JpaMapper;
import nexusmarket.application.adapters.persistence.jpa.repositories.JpaBuyerRepository;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.ports.out.BuyerRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBuyerRepositoryAdapter implements BuyerRepositoryPort {

    private final JpaBuyerRepository repository;

    @Override
    public Buyer save(Buyer buyer) {
        BuyerEntity entity = repository.save(JpaMapper.toEntity(buyer));
        buyer.setId(entity.getId());
        return buyer;
    }

    @Override
    public Optional<Buyer> findById(String id) {
        return repository.findById(id).map(JpaMapper::toDomain);
    }

    @Override
    public Optional<Buyer> findByEmail(String email) {
        return repository.findByEmail(email).map(JpaMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public List<Buyer> findAll() {
        return repository.findAll().stream().map(JpaMapper::toDomain).toList();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}
