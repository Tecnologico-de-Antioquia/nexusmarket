package nexusmarket.application.domain.services.inventory;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.InsufficientStockException;
import nexusmarket.application.domain.exceptions.InvalidRequestException;
import nexusmarket.application.domain.exceptions.UnauthorizedOperationException;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.InventoryMovement;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.models.Warehouse;
import nexusmarket.application.domain.ports.out.InventoryMovementRepositoryPort;
import nexusmarket.application.domain.ports.out.InventoryRepositoryPort;
import nexusmarket.application.domain.ports.out.ProductRepositoryPort;
import nexusmarket.application.domain.ports.out.WarehouseRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.MovementType;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryMovementRepositoryPort inventoryMovementRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final WarehouseRepositoryPort warehouseRepositoryPort;
    private final AuthorizationService authorizationService;

    public Inventory register(User user, Inventory inventory) {
        authorizationService.requireAnyRole(user, SystemRole.SELLER, SystemRole.LOGISTIC_OPERATOR);
        if (inventory == null || inventory.getProduct() == null || inventory.getWarehouse() == null) {
            throw new InvalidRequestException("El inventario debe estar vinculado a un producto y una bodega.");
        }
        Product product = productRepositoryPort.findById(inventory.getProduct().getId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado."));
        Warehouse warehouse = warehouseRepositoryPort.findById(inventory.getWarehouse().getId())
                .orElseThrow(() -> new EntityNotFoundException("Bodega no encontrada."));
        if (SystemRole.SELLER.equals(user.getRole())) {
            requireOwnership(user, product, warehouse);
        }
        inventory.setId(UUID.randomUUID().toString());
        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        if (inventory.getAvailableQuantity() == null) {
            inventory.setAvailableQuantity(0);
        }
        Inventory saved = inventoryRepositoryPort.save(inventory);
        if (saved.getAvailableQuantity() > 0) {
            registerMovement(user, saved, MovementType.RECEIPT, saved.getAvailableQuantity(), null);
        }
        return saved;
    }

    public Inventory receive(User user, String productId, String warehouseId, int quantity) {
        Inventory inventory = loadAndAuthorize(user, productId, warehouseId);
        inventory.receive(quantity);
        Inventory saved = inventoryRepositoryPort.save(inventory);
        registerMovement(user, saved, MovementType.RECEIPT, quantity, null);
        return saved;
    }

    public Inventory reserve(User user, String productId, String warehouseId, int quantity) {
        Inventory inventory = loadAndAuthorize(user, productId, warehouseId);
        inventory.reserve(quantity);
        Inventory saved = inventoryRepositoryPort.save(inventory);
        registerMovement(user, saved, MovementType.RESERVATION, quantity, null);
        return saved;
    }

    public Inventory releaseReservation(User user, String productId, String warehouseId, int quantity) {
        Inventory inventory = loadAndAuthorize(user, productId, warehouseId);
        inventory.releaseReservation(quantity);
        Inventory saved = inventoryRepositoryPort.save(inventory);
        registerMovement(user, saved, MovementType.RESERVATION, quantity, "RELEASE");
        return saved;
    }

    public Inventory saleOut(User user, String productId, String warehouseId, int quantity) {
        Inventory inventory = loadAndAuthorize(user, productId, warehouseId);
        inventory.saleOut(quantity);
        Inventory saved = inventoryRepositoryPort.save(inventory);
        registerMovement(user, saved, MovementType.SALE_OUT, quantity, null);
        return saved;
    }

    public Inventory adjust(User user, String productId, String warehouseId, int delta) {
        Inventory inventory = loadAndAuthorize(user, productId, warehouseId);
        inventory.adjust(delta);
        Inventory saved = inventoryRepositoryPort.save(inventory);
        registerMovement(user, saved, MovementType.ADJUSTMENT, Math.abs(delta), null);
        return saved;
    }

    public Inventory returnStock(User user, String productId, String warehouseId, int quantity) {
        Inventory inventory = loadAndAuthorize(user, productId, warehouseId);
        inventory.returnStock(quantity);
        Inventory saved = inventoryRepositoryPort.save(inventory);
        registerMovement(user, saved, MovementType.RETURN, quantity, null);
        return saved;
    }

    public void reserveForProduct(User actor, String productId, int quantity) {
        if (quantity <= 0) {
            return;
        }
        List<Inventory> inventories = inventoryRepositoryPort.findByProductId(productId);
        int totalAvailable = inventories.stream()
                .mapToInt(inventory -> inventory.getAvailableQuantity() == null ? 0 : inventory.getAvailableQuantity())
                .sum();
        if (totalAvailable < quantity) {
            throw new InsufficientStockException("No hay existencias suficientes para el producto " + productId + ".");
        }
        int remaining = quantity;
        for (Inventory inventory : inventories) {
            if (remaining == 0) {
                break;
            }
            int available = inventory.getAvailableQuantity() == null ? 0 : inventory.getAvailableQuantity();
            int take = Math.min(available, remaining);
            if (take > 0) {
                inventory.reserve(take);
                Inventory saved = inventoryRepositoryPort.save(inventory);
                registerMovement(actor, saved, MovementType.RESERVATION, take, null);
                remaining -= take;
            }
        }
    }

    public void releaseForProduct(User actor, String productId, int quantity) {
        if (quantity <= 0) {
            return;
        }
        List<Inventory> inventories = inventoryRepositoryPort.findByProductId(productId);
        int remaining = quantity;
        for (Inventory inventory : inventories) {
            if (remaining == 0) {
                break;
            }
            int reserved = inventory.getReservedQuantity() == null ? 0 : inventory.getReservedQuantity();
            int take = Math.min(reserved, remaining);
            if (take > 0) {
                inventory.releaseReservation(take);
                Inventory saved = inventoryRepositoryPort.save(inventory);
                registerMovement(actor, saved, MovementType.RESERVATION, take, "RELEASE");
                remaining -= take;
            }
        }
    }

    public void saleOutForProduct(User actor, String productId, int quantity) {
        if (quantity <= 0) {
            return;
        }
        List<Inventory> inventories = inventoryRepositoryPort.findByProductId(productId);
        int remaining = quantity;
        for (Inventory inventory : inventories) {
            if (remaining == 0) {
                break;
            }
            int reserved = inventory.getReservedQuantity() == null ? 0 : inventory.getReservedQuantity();
            int take = Math.min(reserved, remaining);
            if (take > 0) {
                inventory.saleOut(take);
                Inventory saved = inventoryRepositoryPort.save(inventory);
                registerMovement(actor, saved, MovementType.SALE_OUT, take, null);
                remaining -= take;
            }
        }
    }

    public void returnForProduct(User actor, String productId, int quantity) {
        if (quantity <= 0) {
            return;
        }
        List<Inventory> inventories = inventoryRepositoryPort.findByProductId(productId);
        if (inventories.isEmpty()) {
            throw new EntityNotFoundException("No existe inventario para el producto " + productId + ".");
        }
        Inventory inventory = inventories.get(0);
        inventory.returnStock(quantity);
        Inventory saved = inventoryRepositoryPort.save(inventory);
        registerMovement(actor, saved, MovementType.RETURN, quantity, null);
    }

    public int availableForProduct(String productId) {
        return inventoryRepositoryPort.findByProductId(productId).stream()
                .mapToInt(inventory -> inventory.getAvailableQuantity() == null ? 0 : inventory.getAvailableQuantity())
                .sum();
    }

    private Inventory loadAndAuthorize(User user, String productId, String warehouseId) {
        authorizationService.requireAnyRole(user, SystemRole.SELLER, SystemRole.LOGISTIC_OPERATOR);
        Inventory inventory = inventoryRepositoryPort.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado."));
        if (SystemRole.SELLER.equals(user.getRole())) {
            requireOwnership(user, inventory.getProduct(), inventory.getWarehouse());
        }
        return inventory;
    }

    private void requireOwnership(User user, Product product, Warehouse warehouse) {
        boolean productOwned = product != null && product.getSeller() != null
                && user.getId().equals(product.getSeller().getId());
        boolean warehouseOwned = warehouse != null && warehouse.getOwner() != null
                && user.getId().equals(warehouse.getOwner().getId());
        if (!productOwned || !warehouseOwned) {
            throw new UnauthorizedOperationException("El vendedor no administra este producto o bodega.");
        }
    }

    private void registerMovement(User actor, Inventory inventory, MovementType type, int quantity, String reference) {
        InventoryMovement movement = new InventoryMovement();
        movement.setId(UUID.randomUUID().toString());
        movement.setInventory(inventory);
        movement.setType(type);
        movement.setQuantity(quantity);
        movement.setOccurredAt(LocalDateTime.now());
        movement.setPerformedBy(actor);
        movement.setReference(reference);
        inventoryMovementRepositoryPort.save(movement);
    }
}
