package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.Setter;
import nexusmarket.application.domain.valueObjects.MovementsState;

@Getter
@Setter
public class Inventory {
    private String id;
    private String productId;
    private String warehouseId;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private MovementsState state;

    public Inventory(String id, String productId, String warehouseId, Integer availableQuantity) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Inventory id is required");
        }
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product id is required");
        }
        if (warehouseId == null || warehouseId.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse id is required");
        }
        if (availableQuantity == null || availableQuantity < 0) {
            throw new IllegalArgumentException("Available quantity must be zero or greater");
        }

        this.id = id.trim();
        this.productId = productId.trim();
        this.warehouseId = warehouseId.trim();
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = 0;
        this.state = MovementsState.RECEIPT;
    }

    public void receive(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.availableQuantity += quantity;
        this.state = MovementsState.RECEIPT;
    }

    public void reserve(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (this.availableQuantity < quantity) {
            throw new IllegalStateException("Insufficient available inventory");
        }
        this.availableQuantity -= quantity;
        this.reservedQuantity += quantity;
        this.state = MovementsState.RESERVATION;
    }

    public void releaseReservation(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (this.reservedQuantity < quantity) {
            throw new IllegalStateException("Reservation quantity exceeds current reserved stock");
        }
        this.reservedQuantity -= quantity;
        this.availableQuantity += quantity;
        this.state = MovementsState.RETURN;
    }

    public void adjust(Integer delta) {
        if (delta == null) {
            throw new IllegalArgumentException("Adjustment value is required");
        }
        int newQuantity = this.availableQuantity + delta;
        if (newQuantity < 0) {
            throw new IllegalStateException("Inventory cannot be negative");
        }
        this.availableQuantity = newQuantity;
        this.state = MovementsState.ADJUSMENT;
    }
}
