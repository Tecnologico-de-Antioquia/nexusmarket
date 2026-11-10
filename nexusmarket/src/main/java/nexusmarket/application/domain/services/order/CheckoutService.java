package nexusmarket.application.domain.services.order;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.InvalidRequestException;
import nexusmarket.application.domain.models.Address;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.models.Cart;
import nexusmarket.application.domain.models.CartItem;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.OrderItem;
import nexusmarket.application.domain.models.PhysicalProduct;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.BuyerRepositoryPort;
import nexusmarket.application.domain.ports.out.CartRepositoryPort;
import nexusmarket.application.domain.ports.out.OrderRepositoryPort;
import nexusmarket.application.domain.services.inventory.InventoryService;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.OrderState;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartRepositoryPort cartRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final BuyerRepositoryPort buyerRepositoryPort;
    private final InventoryService inventoryService;
    private final AuthorizationService authorizationService;

    public Order checkout(User user, Address destinationAddress) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        Buyer buyer = buyerRepositoryPort.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Comprador no encontrado."));
        Cart cart = cartRepositoryPort.findActiveByBuyerId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado."));
        if (cart.isEmpty()) {
            throw new InvalidRequestException("El carrito esta vacio.");
        }

        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setBuyer(buyer);
        order.setState(OrderState.CART);
        order.setCreatedAt(LocalDateTime.now());

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(UUID.randomUUID().toString());
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getProduct().getPrice());
            orderItem.recalculateSubtotal();
            order.getItems().add(orderItem);
            if (cartItem.getProduct() instanceof PhysicalProduct) {
                inventoryService.reserveForProduct(user, cartItem.getProduct().getId(), cartItem.getQuantity());
            }
        }

        order.recalculateTotal();
        order.submit();
        Order saved = orderRepositoryPort.save(order);

        cart.clear();
        cart.checkOut();
        cartRepositoryPort.save(cart);
        return saved;
    }
}
