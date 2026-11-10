package nexusmarket.application.adapters.persistence.jpa.adapters;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.persistence.jpa.entities.WarehouseEntity;
import nexusmarket.application.adapters.persistence.jpa.mappers.JpaMapper;
import nexusmarket.application.adapters.persistence.jpa.repositories.JpaWarehouseRepository;
import nexusmarket.application.domain.models.Warehouse;
import nexusmarket.application.domain.ports.out.SellerRepositoryPort;
import nexusmarket.application.domain.ports.out.WarehouseRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaWarehouseRepositoryAdapter implements WarehouseRepositoryPort {

    private final JpaWarehouseRepository repository;
    private final SellerRepositoryPort sellerRepositoryPort;

    @Override
    public Warehouse save(Warehouse warehouse) {
        WarehouseEntity entity = repository.save(JpaMapper.toEntity(warehouse));
        warehouse.setId(entity.getId());
        return warehouse;
    }

    @Override
    public Optional<Warehouse> findById(String id) {
        return repository.findById(id).map(this::enrich);
    }

    @Override
    public List<Warehouse> findAll() {
        return repository.findAll().stream().map(this::enrich).toList();
    }

    @Override
    public List<Warehouse> findBySellerId(String sellerId) {
        return repository.findByOwnerId(sellerId).stream().map(this::enrich).toList();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    private Warehouse enrich(WarehouseEntity entity) {
        Warehouse warehouse = JpaMapper.toDomain(entity);
        if (warehouse != null && warehouse.getOwner() != null) {
            sellerRepositoryPort.findById(warehouse.getOwner().getId()).ifPresent(warehouse::setOwner);
        }
        return warehouse;
    }
}
