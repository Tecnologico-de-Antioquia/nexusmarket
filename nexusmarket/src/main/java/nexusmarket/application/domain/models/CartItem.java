package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem {
    private String productId;
    private Integer quantity;

    public CartItem(String productId, Integer quantity) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product id is required");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.productId = productId.trim();
        this.quantity = quantity;
    }
}
