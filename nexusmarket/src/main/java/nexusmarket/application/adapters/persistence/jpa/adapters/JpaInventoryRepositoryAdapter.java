package nexusmarket.application.adapters.persistence.jpa.adapters;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.persistence.jpa.entities.InventoryEntity;
import nexusmarket.application.adapters.persistence.jpa.mappers.JpaMapper;
import nexusmarket.application.adapters.persistence.jpa.repositories.JpaInventoryRepository;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.ports.out.InventoryRepositoryPort;
import nexusmarket.application.domain.ports.out.ProductRepositoryPort;
import nexusmarket.application.domain.ports.out.WarehouseRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaInventoryRepositoryAdapter implements InventoryRepositoryPort {

    private final JpaInventoryRepository repository;
    private final ProductRepositoryPort productRepositoryPort;
    private final WarehouseRepositoryPort warehouseRepositoryPort;

    @Override
    public Inventory save(Inventory inventory) {
        InventoryEntity entity = repository.save(JpaMapper.toEntity(inventory));
        inventory.setId(entity.getId());
        return inventory;
    }

    @Override
    public Optional<Inventory> findById(String id) {
        return repository.findById(id).map(this::enrich);
    }

    @Override
    public Optional<Inventory> findByProductIdAndWarehouseId(String productId, String warehouseId) {
        return repository.findByProductIdAndWarehouseId(productId, warehouseId).map(this::enrich);
    }

    @Override
    public List<Inventory> findByProductId(String productId) {
        return repository.findByProductId(productId).stream().map(this::enrich).toList();
    }

    @Override
    public List<Inventory> findByWarehouseId(String warehouseId) {
        return repository.findByWarehouseId(warehouseId).stream().map(this::enrich).toList();
    }

    @Override
    public List<Inventory> findAll() {
        return repository.findAll().stream().map(this::enrich).toList();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    private Inventory enrich(InventoryEntity entity) {
        Inventory inventory = JpaMapper.toDomain(entity);
        if (inventory != null && inventory.getProduct() != null) {
            productRepositoryPort.findById(inventory.getProduct().getId()).ifPresent(inventory::setProduct);
        }
        if (inventory != null && inventory.getWarehouse() != null) {
            warehouseRepositoryPort.findById(inventory.getWarehouse().getId()).ifPresent(inventory::setWarehouse);
        }
        return inventory;
    }
}
