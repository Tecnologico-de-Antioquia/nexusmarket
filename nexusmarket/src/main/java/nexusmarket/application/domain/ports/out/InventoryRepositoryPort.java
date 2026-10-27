package nexusmarket.application.domain.ports.out;

import java.util.List;
import java.util.Optional;

import nexusmarket.application.domain.models.Inventory;

public interface InventoryRepositoryPort {
    Inventory save(Inventory inventory);
    Optional<Inventory> findByProductIdAndWarehouseId(String productId, String warehouseId);
    List<Inventory> findByProductId(String productId);
    List<Inventory> findAll();
    void delete(String id);
}
