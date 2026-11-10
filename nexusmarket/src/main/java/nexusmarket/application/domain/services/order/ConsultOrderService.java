package nexusmarket.application.domain.services.order;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.UnauthorizedOperationException;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.OrderItem;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.OrderRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultOrderService {

    private final OrderRepositoryPort orderRepositoryPort;
    private final AuthorizationService authorizationService;

    public List<Order> consultMyOrders(User user) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        return orderRepositoryPort.findByBuyerId(user.getId());
    }

    public Order consultMyOrder(User user, String orderId) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado."));
        if (order.getBuyer() == null || !order.getBuyer().getId().equals(user.getId())) {
            throw new UnauthorizedOperationException("El pedido no pertenece al comprador.");
        }
        return order;
    }

    public List<Order> consultAll(User user) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SUPERVISOR,
                SystemRole.LOGISTIC_OPERATOR);
        return orderRepositoryPort.findAll();
    }

    public Order consultById(User user, String orderId) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SUPERVISOR,
                SystemRole.LOGISTIC_OPERATOR);
        return orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado."));
    }

    public List<Order> consultBySeller(User user) {
        authorizationService.requireActiveRole(user, SystemRole.SELLER);
        return orderRepositoryPort.findAll().stream()
                .filter(order -> order.getItems().stream()
                        .map(OrderItem::getProduct)
                        .anyMatch(product -> product.getSeller() != null
                                && user.getId().equals(product.getSeller().getId())))
                .toList();
    }
}
