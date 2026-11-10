package nexusmarket.application.adapters.persistence.jpa.repositories;

import nexusmarket.application.adapters.persistence.jpa.entities.InventoryMovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaInventoryMovementRepository extends JpaRepository<InventoryMovementEntity, String> {

    List<InventoryMovementEntity> findByInventoryId(String inventoryId);
}
