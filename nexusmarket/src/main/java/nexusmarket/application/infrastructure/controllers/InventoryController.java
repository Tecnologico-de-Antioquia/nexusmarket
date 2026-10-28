package nexusmarket.application.infrastructure.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.services.inventory.InventoryService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/inventory")
    public Inventory registerInventory(@RequestBody InventoryRequest request) {
        Inventory inventory = new Inventory(
                request.id(),
                request.productId(),
                request.warehouseId(),
                request.availableQuantity());
        return inventoryService.registerInventory(inventory);
    }

    @GetMapping("/inventory")
    public List<Inventory> findAll() {
        return inventoryService.findAll();
    }

    @GetMapping("/inventory/{productId}/{warehouseId}")
    public Inventory findByProductAndWarehouse(@PathVariable String productId, @PathVariable String warehouseId) {
        return inventoryService.findByProductAndWarehouse(productId, warehouseId);
    }

    @PostMapping("/inventory/receive")
    public Inventory receiveStock(@RequestBody InventoryMovementRequest request) {
        return inventoryService.receiveStock(request.productId(), request.warehouseId(), request.quantity());
    }

    @PostMapping("/inventory/reserve")
    public Inventory reserveStock(@RequestBody InventoryMovementRequest request) {
        return inventoryService.reserveStock(request.productId(), request.warehouseId(), request.quantity());
    }

    public record InventoryRequest(String id, String productId, String warehouseId, Integer availableQuantity) {}

    public record InventoryMovementRequest(String productId, String warehouseId, Integer quantity) {}
}
