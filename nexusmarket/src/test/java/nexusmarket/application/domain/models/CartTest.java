package nexusmarket.application.domain.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartTest {

    @Test
    void addingSameProductReplacesQuantity() {
        Cart cart = new Cart();
        Product product = new PhysicalProduct();
        product.setId("product-1");

        CartItem first = new CartItem();
        first.setProduct(product);
        first.setQuantity(1);
        cart.addItem(first);

        CartItem second = new CartItem();
        second.setProduct(product);
        second.setQuantity(3);
        cart.addItem(second);

        assertEquals(1, cart.getItems().size());
        assertEquals(3, cart.getItems().get(0).getQuantity());
    }

    @Test
    void clearRemovesAllItems() {
        Cart cart = new Cart();
        Product product = new PhysicalProduct();
        product.setId("product-1");
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(2);
        cart.addItem(item);

        cart.clear();

        assertTrue(cart.isEmpty());
    }
}
