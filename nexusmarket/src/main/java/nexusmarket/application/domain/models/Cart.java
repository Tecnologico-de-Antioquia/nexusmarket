package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.valueobjects.CartState;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Cart {

    private String id;
    private Buyer buyer;
    private List<CartItem> items = new ArrayList<>();
    private CartState state = CartState.ACTIVE;

    public void addItem(CartItem item) {
        if (item == null) {
            throw new IllegalArgumentException("El item es obligatorio.");
        }
        this.items.removeIf(existing -> existing.getProduct().getId().equals(item.getProduct().getId()));
        this.items.add(item);
    }

    public void updateItem(String productId, int quantity) {
        CartItem item = this.items.stream()
                .filter(existing -> existing.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El producto no esta en el carrito."));
        item.setQuantity(quantity);
    }

    public void removeItem(String productId) {
        this.items.removeIf(existing -> existing.getProduct().getId().equals(productId));
    }

    public void clear() {
        this.items.clear();
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public void checkOut() {
        this.state = CartState.CHECKED_OUT;
    }
}
