package nexusmarket.application.domain.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Car {
    private String id;
    private String buyerId;
    private List<CartItem> items = new ArrayList<>();
    private String state;

    public Car(String id, String buyerId) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Car id is required");
        }
        if (buyerId == null || buyerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Buyer id is required");
        }
        this.id = id.trim();
        this.buyerId = buyerId.trim();
        this.state = "ACTIVE";
    }

    public void addItem(CartItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Cart item is required");
        }
        this.items.add(item);
    }

    public void removeItem(String productId) {
        this.items.removeIf(item -> item.getProductId().equals(productId));
    }

    public void clear() {
        this.items.clear();
    }
}
