package nexusmarket.application.adapters.persistence.jpa.adapters;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.persistence.jpa.entities.InventoryMovementEntity;
import nexusmarket.application.adapters.persistence.jpa.mappers.JpaMapper;
import nexusmarket.application.adapters.persistence.jpa.repositories.JpaInventoryMovementRepository;
import nexusmarket.application.domain.models.InventoryMovement;
import nexusmarket.application.domain.ports.out.InventoryMovementRepositoryPort;
import nexusmarket.application.domain.ports.out.InventoryRepositoryPort;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaInventoryMovementRepositoryAdapter implements InventoryMovementRepositoryPort {

    private final JpaInventoryMovementRepository repository;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public InventoryMovement save(InventoryMovement movement) {
        InventoryMovementEntity entity = repository.save(JpaMapper.toEntity(movement));
        movement.setId(entity.getId());
        return movement;
    }

    @Override
    public List<InventoryMovement> findByInventoryId(String inventoryId) {
        return repository.findByInventoryId(inventoryId).stream().map(this::enrich).toList();
    }

    @Override
    public List<InventoryMovement> findAll() {
        return repository.findAll().stream().map(this::enrich).toList();
    }

    private InventoryMovement enrich(InventoryMovementEntity entity) {
        InventoryMovement movement = JpaMapper.toDomain(entity);
        if (movement != null && movement.getInventory() != null) {
            inventoryRepositoryPort.findById(movement.getInventory().getId())
                    .ifPresent(movement::setInventory);
        }
        if (movement != null && movement.getPerformedBy() != null) {
            userRepositoryPort.findById(movement.getPerformedBy().getId())
                    .ifPresent(movement::setPerformedBy);
        }
        return movement;
    }
}
