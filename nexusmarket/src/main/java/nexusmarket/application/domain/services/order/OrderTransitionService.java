package nexusmarket.application.domain.services.order;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.OrderItem;
import nexusmarket.application.domain.models.PhysicalProduct;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.OrderRepositoryPort;
import nexusmarket.application.domain.ports.out.ShipmentRepositoryPort;
import nexusmarket.application.domain.services.inventory.InventoryService;
import nexusmarket.application.domain.services.operation.RegisterAuditLogService;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderTransitionService {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ShipmentRepositoryPort shipmentRepositoryPort;
    private final InventoryService inventoryService;
    private final RegisterAuditLogService registerAuditLogService;
    private final AuthorizationService authorizationService;

    public Order dispatch(User user, String orderId) {
        authorizationService.requireActiveRole(user, SystemRole.LOGISTIC_OPERATOR);
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado."));
        order.dispatch();
        for (OrderItem item : order.getItems()) {
            if (item.getProduct() instanceof PhysicalProduct) {
                inventoryService.saleOutForProduct(user, item.getProduct().getId(), item.getQuantity());
            }
        }
        if (order.getShipment() != null) {
            order.getShipment().dispatch();
            shipmentRepositoryPort.save(order.getShipment());
        }
        Order saved = orderRepositoryPort.save(order);
        registerAuditLogService.register(user, "ORDER_DISPATCHED", "ORDER", saved.getId(), Map.of());
        return saved;
    }

    public Order deliver(User user, String orderId) {
        authorizationService.requireActiveRole(user, SystemRole.LOGISTIC_OPERATOR);
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado."));
        order.deliver();
        if (order.getShipment() != null) {
            order.getShipment().deliver();
            shipmentRepositoryPort.save(order.getShipment());
        }
        Order saved = orderRepositoryPort.save(order);
        registerAuditLogService.register(user, "ORDER_DELIVERED", "ORDER", saved.getId(), Map.of());
        return saved;
    }
}
