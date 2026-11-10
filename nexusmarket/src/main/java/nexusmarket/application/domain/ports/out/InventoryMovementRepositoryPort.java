package nexusmarket.application.domain.ports.out;

import nexusmarket.application.domain.models.InventoryMovement;

import java.util.List;

public interface InventoryMovementRepositoryPort {

    InventoryMovement save(InventoryMovement movement);

    List<InventoryMovement> findByInventoryId(String inventoryId);

    List<InventoryMovement> findAll();
}
