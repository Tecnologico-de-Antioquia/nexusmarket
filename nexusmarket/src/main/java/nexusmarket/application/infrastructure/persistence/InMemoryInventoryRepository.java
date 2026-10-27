package nexusmarket.application.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.ports.out.InventoryRepositoryPort;

@Repository
public class InMemoryInventoryRepository implements InventoryRepositoryPort {
    private final Map<String, Inventory> inventories = new ConcurrentHashMap<>();

    @Override
    public Inventory save(Inventory inventory) {
        inventories.put(inventory.getId(), inventory);
        return inventory;
    }

    @Override
    public Optional<Inventory> findByProductIdAndWarehouseId(String productId, String warehouseId) {
        return inventories.values().stream()
                .filter(inventory -> inventory.getProductId().equals(productId) && inventory.getWarehouseId().equals(warehouseId))
                .findFirst();
    }

    @Override
    public List<Inventory> findByProductId(String productId) {
        return inventories.values().stream()
                .filter(inventory -> inventory.getProductId().equals(productId))
                .toList();
    }

    @Override
    public List<Inventory> findAll() {
        return new ArrayList<>(inventories.values());
    }

    @Override
    public void delete(String id) {
        inventories.remove(id);
    }
}
