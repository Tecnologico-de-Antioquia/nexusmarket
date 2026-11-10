package nexusmarket.application.domain.services.order;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.UnauthorizedOperationException;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.OrderItem;
import nexusmarket.application.domain.models.PhysicalProduct;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.OrderRepositoryPort;
import nexusmarket.application.domain.services.inventory.InventoryService;
import nexusmarket.application.domain.services.operation.RegisterAuditLogService;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CancelOrderService {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryService inventoryService;
    private final RegisterAuditLogService registerAuditLogService;
    private final AuthorizationService authorizationService;

    public Order cancel(User user, String orderId) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado."));
        if (order.getBuyer() == null || !order.getBuyer().getId().equals(user.getId())) {
            throw new UnauthorizedOperationException("El pedido no pertenece al comprador.");
        }
        order.cancel();
        releaseReservations(user, order);
        Order saved = orderRepositoryPort.save(order);
        registerAuditLogService.register(user, "ORDER_CANCELLED", "ORDER", saved.getId(), Map.of());
        return saved;
    }

    public Order cancelByAdmin(User admin, String orderId) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado."));
        order.cancel();
        releaseReservations(admin, order);
        Order saved = orderRepositoryPort.save(order);
        registerAuditLogService.register(admin, "ORDER_CANCELLED", "ORDER", saved.getId(), Map.of());
        return saved;
    }

    private void releaseReservations(User actor, Order order) {
        for (OrderItem item : order.getItems()) {
            if (item.getProduct() instanceof PhysicalProduct) {
                inventoryService.releaseForProduct(actor, item.getProduct().getId(), item.getQuantity());
            }
        }
    }
}
