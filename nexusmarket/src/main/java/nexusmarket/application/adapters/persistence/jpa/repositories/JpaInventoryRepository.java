package nexusmarket.application.adapters.persistence.jpa.repositories;

import nexusmarket.application.adapters.persistence.jpa.entities.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaInventoryRepository extends JpaRepository<InventoryEntity, String> {

    Optional<InventoryEntity> findByProductIdAndWarehouseId(String productId, String warehouseId);

    List<InventoryEntity> findByProductId(String productId);

    List<InventoryEntity> findByWarehouseId(String warehouseId);
}
