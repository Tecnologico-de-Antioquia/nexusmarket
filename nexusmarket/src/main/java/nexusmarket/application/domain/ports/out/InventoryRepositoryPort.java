package nexusmarket.application.domain.ports.out;

import nexusmarket.application.domain.models.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryRepositoryPort {

    Inventory save(Inventory inventory);

    Optional<Inventory> findById(String id);

    Optional<Inventory> findByProductIdAndWarehouseId(String productId, String warehouseId);

    List<Inventory> findByProductId(String productId);

    List<Inventory> findByWarehouseId(String warehouseId);

    List<Inventory> findAll();

    void delete(String id);
}
