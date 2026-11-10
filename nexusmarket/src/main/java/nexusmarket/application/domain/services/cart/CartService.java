package nexusmarket.application.domain.services.cart;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.DomainException;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.InsufficientStockException;
import nexusmarket.application.domain.exceptions.InvalidRequestException;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.models.Cart;
import nexusmarket.application.domain.models.CartItem;
import nexusmarket.application.domain.models.PhysicalProduct;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.CartRepositoryPort;
import nexusmarket.application.domain.ports.out.ProductRepositoryPort;
import nexusmarket.application.domain.services.inventory.InventoryService;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.CartState;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepositoryPort cartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final InventoryService inventoryService;
    private final AuthorizationService authorizationService;

    public Cart consult(User user) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        return getOrCreate(user);
    }

    public Cart add(User user, String productId, int quantity) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        if (quantity <= 0) {
            throw new InvalidRequestException("La cantidad debe ser mayor que cero.");
        }
        Cart cart = getOrCreate(user);
        Product product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado."));
        if (!product.isPublished()) {
            throw new DomainException("El producto no esta publicado.");
        }
        if (product instanceof PhysicalProduct) {
            int available = inventoryService.availableForProduct(productId);
            if (available < quantity) {
                throw new InsufficientStockException("No hay existencias suficientes para el producto.");
            }
        }
        CartItem item = new CartItem();
        item.setId(UUID.randomUUID().toString());
        item.setProduct(product);
        item.setQuantity(quantity);
        cart.addItem(item);
        return cartRepositoryPort.save(cart);
    }

    public Cart update(User user, String productId, int quantity) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        if (quantity <= 0) {
            throw new InvalidRequestException("La cantidad debe ser mayor que cero.");
        }
        Cart cart = getOrCreate(user);
        cart.updateItem(productId, quantity);
        return cartRepositoryPort.save(cart);
    }

    public Cart remove(User user, String productId) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        Cart cart = getOrCreate(user);
        cart.removeItem(productId);
        return cartRepositoryPort.save(cart);
    }

    public Cart clear(User user) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        Cart cart = getOrCreate(user);
        cart.clear();
        return cartRepositoryPort.save(cart);
    }

    public Cart getActiveCart(User user) {
        return cartRepositoryPort.findActiveByBuyerId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado."));
    }

    private Cart getOrCreate(User user) {
        return cartRepositoryPort.findActiveByBuyerId(user.getId()).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setId(UUID.randomUUID().toString());
            Buyer buyer = new Buyer();
            buyer.setId(user.getId());
            cart.setBuyer(buyer);
            cart.setState(CartState.ACTIVE);
            return cartRepositoryPort.save(cart);
        });
    }
}
