package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Physical extends Product {
    private Integer stock;
    private Variant variant;

    public Physical(String id, String name, String description, String sellerId, Integer stock, Variant variant) {
        super(id, name, description, sellerId);
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Stock must be zero or greater");
        }
        this.stock = stock;
        this.variant = variant;
    }

    public void addStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.stock += quantity;
    }

    public void reserveStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (this.stock < quantity) {
            throw new IllegalStateException("Insufficient stock for reservation");
        }
        this.stock -= quantity;
    }

    public void releaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.stock += quantity;
    }
}
