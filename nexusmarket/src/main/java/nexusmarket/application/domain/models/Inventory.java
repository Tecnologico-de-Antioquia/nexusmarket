package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.exceptions.InsufficientStockException;
import nexusmarket.application.domain.valueobjects.InventoryCondition;

@Getter
@Setter
@NoArgsConstructor
public class Inventory {

    private String id;
    private Product product;
    private Warehouse warehouse;
    private Integer availableQuantity = 0;
    private Integer reservedQuantity = 0;
    private Integer damagedQuantity = 0;
    private InventoryCondition condition = InventoryCondition.AVAILABLE;

    public void receive(int quantity) {
        requirePositive(quantity);
        this.availableQuantity += quantity;
    }

    public void reserve(int quantity) {
        requirePositive(quantity);
        if (InventoryCondition.DAMAGED.equals(condition)) {
            throw new InsufficientStockException("No se puede reservar inventario marcado como danado.");
        }
        if (availableQuantity < quantity) {
            throw new InsufficientStockException("No hay existencias suficientes para reservar.");
        }
        this.availableQuantity -= quantity;
        this.reservedQuantity += quantity;
    }

    public void releaseReservation(int quantity) {
        requirePositive(quantity);
        if (reservedQuantity < quantity) {
            throw new InsufficientStockException("No hay reservas suficientes para liberar.");
        }
        this.reservedQuantity -= quantity;
        this.availableQuantity += quantity;
    }

    public void saleOut(int quantity) {
        requirePositive(quantity);
        if (reservedQuantity < quantity) {
            throw new InsufficientStockException("No hay reservas suficientes para despachar la venta.");
        }
        this.reservedQuantity -= quantity;
    }

    public void adjust(int delta) {
        if (this.availableQuantity + delta < 0) {
            throw new InsufficientStockException("Las existencias no pueden ser negativas.");
        }
        this.availableQuantity += delta;
    }

    public void returnStock(int quantity) {
        requirePositive(quantity);
        this.availableQuantity += quantity;
    }

    public void markDamaged(int quantity) {
        requirePositive(quantity);
        if (availableQuantity < quantity) {
            throw new InsufficientStockException("No hay existencias suficientes para marcar como danadas.");
        }
        this.availableQuantity -= quantity;
        this.damagedQuantity += quantity;
        this.condition = InventoryCondition.DAMAGED;
    }

    private void requirePositive(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
    }
}
