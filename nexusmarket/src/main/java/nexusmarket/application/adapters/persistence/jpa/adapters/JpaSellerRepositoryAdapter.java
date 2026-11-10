package nexusmarket.application.adapters.persistence.jpa.adapters;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.persistence.jpa.entities.SellerEntity;
import nexusmarket.application.adapters.persistence.jpa.mappers.JpaMapper;
import nexusmarket.application.adapters.persistence.jpa.repositories.JpaSellerRepository;
import nexusmarket.application.domain.models.Seller;
import nexusmarket.application.domain.ports.out.SellerRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaSellerRepositoryAdapter implements SellerRepositoryPort {

    private final JpaSellerRepository repository;

    @Override
    public Seller save(Seller seller) {
        SellerEntity entity = repository.save(JpaMapper.toEntity(seller));
        seller.setId(entity.getId());
        return seller;
    }

    @Override
    public Optional<Seller> findById(String id) {
        return repository.findById(id).map(JpaMapper::toDomain);
    }

    @Override
    public Optional<Seller> findByEmail(String email) {
        return repository.findByEmail(email).map(JpaMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public List<Seller> findAll() {
        return repository.findAll().stream().map(JpaMapper::toDomain).toList();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}
