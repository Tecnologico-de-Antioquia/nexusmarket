package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.exceptions.InvalidCartException;
import nexusmarket.application.domain.exceptions.InvalidCartStateException;
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
        requireActive();
        if (item == null || item.getProduct() == null || item.getProduct().getId() == null) {
            throw new InvalidCartException("El item y su producto son obligatorios.");
        }
        requirePositiveQuantity(item.getQuantity());
        this.items.removeIf(existing -> existing.getProduct().getId().equals(item.getProduct().getId()));
        this.items.add(item);
    }

    public void updateItem(String productId, int quantity) {
        requireActive();
        requirePositiveQuantity(quantity);
        CartItem item = this.items.stream()
                .filter(existing -> existing.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new InvalidCartException("El producto no esta en el carrito."));
        item.setQuantity(quantity);
    }

    public void removeItem(String productId) {
        requireActive();
        this.items.removeIf(existing -> existing.getProduct().getId().equals(productId));
    }

    public void clear() {
        requireActive();
        this.items.clear();
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public void checkOut() {
        requireActive();
        this.state = CartState.CHECKED_OUT;
    }

    private void requireActive() {
        if (!CartState.ACTIVE.equals(this.state)) {
            throw new InvalidCartStateException("Solo un carrito activo puede modificarse.");
        }
    }

    private void requirePositiveQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidCartException("La cantidad debe ser mayor que cero.");
        }
    }
}
