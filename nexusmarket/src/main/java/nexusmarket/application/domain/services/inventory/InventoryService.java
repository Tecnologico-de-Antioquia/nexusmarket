package nexusmarket.application.domain.services.inventory;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.DomainException;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.ports.out.InventoryRepositoryPort;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    public Inventory registerInventory(Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory is required");
        }
        return inventoryRepositoryPort.save(inventory);
    }

    public Inventory receiveStock(String productId, String warehouseId, Integer quantity) {
        Inventory inventory = findByProductAndWarehouse(productId, warehouseId);
        inventory.receive(quantity);
        return inventoryRepositoryPort.save(inventory);
    }

    public Inventory reserveStock(String productId, String warehouseId, Integer quantity) {
        Inventory inventory = findByProductAndWarehouse(productId, warehouseId);
        inventory.reserve(quantity);
        return inventoryRepositoryPort.save(inventory);
    }

    public Inventory releaseReservation(String productId, String warehouseId, Integer quantity) {
        Inventory inventory = findByProductAndWarehouse(productId, warehouseId);
        inventory.releaseReservation(quantity);
        return inventoryRepositoryPort.save(inventory);
    }

    public Inventory findByProductAndWarehouse(String productId, String warehouseId) {
        return inventoryRepositoryPort.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new DomainException("Inventory not found for product and warehouse"));
    }

    public List<Inventory> findByProductId(String productId) {
        return inventoryRepositoryPort.findByProductId(productId);
    }

    public List<Inventory> findAll() {
        return inventoryRepositoryPort.findAll();
    }
}