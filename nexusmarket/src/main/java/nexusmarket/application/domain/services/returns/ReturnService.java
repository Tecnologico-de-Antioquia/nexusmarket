package nexusmarket.application.domain.services.returns;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.InvalidRequestException;
import nexusmarket.application.domain.exceptions.InvalidStateTransitionException;
import nexusmarket.application.domain.exceptions.UnauthorizedOperationException;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.OrderItem;
import nexusmarket.application.domain.models.PhysicalProduct;
import nexusmarket.application.domain.models.Refund;
import nexusmarket.application.domain.models.ReturnItem;
import nexusmarket.application.domain.models.ReturnRequest;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.OrderRepositoryPort;
import nexusmarket.application.domain.ports.out.RefundRepositoryPort;
import nexusmarket.application.domain.ports.out.ReturnRequestRepositoryPort;
import nexusmarket.application.domain.services.inventory.InventoryService;
import nexusmarket.application.domain.services.operation.RegisterAuditLogService;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.OrderState;
import nexusmarket.application.domain.valueobjects.RefundState;
import nexusmarket.application.domain.valueobjects.ReturnState;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReturnService {

    private final ReturnRequestRepositoryPort returnRequestRepositoryPort;
    private final RefundRepositoryPort refundRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryService inventoryService;
    private final RegisterAuditLogService registerAuditLogService;
    private final AuthorizationService authorizationService;

    public ReturnRequest request(User user, String orderId, List<ReturnItem> items, String reason) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado."));
        if (order.getBuyer() == null || !order.getBuyer().getId().equals(user.getId())) {
            throw new UnauthorizedOperationException("El pedido no pertenece al comprador.");
        }
        if (!OrderState.DELIVERED.equals(order.getState())) {
            throw new InvalidStateTransitionException("Solo un pedido entregado puede devolverse.");
        }
        if (items == null || items.isEmpty()) {
            throw new InvalidRequestException("La devolucion debe incluir al menos un item.");
        }
        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setId(UUID.randomUUID().toString());
        returnRequest.setOrder(order);
        returnRequest.setReason(reason);
        returnRequest.setState(ReturnState.REQUESTED);
        returnRequest.setRequestedAt(LocalDateTime.now());
        for (ReturnItem item : items) {
            if (item.getOrderItem() == null || item.getOrderItem().getId() == null) {
                throw new InvalidRequestException("El item de devolucion no es valido.");
            }
            OrderItem orderItem = order.getItems().stream()
                    .filter(candidate -> candidate.getId().equals(item.getOrderItem().getId()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidRequestException("El item no pertenece al pedido."));
            item.setOrderItem(orderItem);
            item.setId(UUID.randomUUID().toString());
            returnRequest.getItems().add(item);
        }
        ReturnRequest saved = returnRequestRepositoryPort.save(returnRequest);
        registerAuditLogService.register(user, "RETURN_REQUESTED", "RETURN", saved.getId(),
                Map.of("orderId", orderId));
        return saved;
    }

    public ReturnRequest approve(User admin, String returnId) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        ReturnRequest returnRequest = load(returnId);
        returnRequest.approve();
        ReturnRequest saved = returnRequestRepositoryPort.save(returnRequest);
        registerAuditLogService.register(admin, "RETURN_APPROVED", "RETURN", saved.getId(), Map.of());
        return saved;
    }

    public ReturnRequest reject(User admin, String returnId) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        ReturnRequest returnRequest = load(returnId);
        returnRequest.reject();
        ReturnRequest saved = returnRequestRepositoryPort.save(returnRequest);
        registerAuditLogService.register(admin, "RETURN_REJECTED", "RETURN", saved.getId(), Map.of());
        return saved;
    }

    public ReturnRequest receive(User admin, String returnId) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        ReturnRequest returnRequest = load(returnId);
        returnRequest.receive();
        BigDecimal refundAmount = BigDecimal.ZERO;
        for (ReturnItem item : returnRequest.getItems()) {
            OrderItem orderItem = item.getOrderItem();
            if (orderItem == null || orderItem.getProduct() == null) {
                throw new InvalidRequestException("El item de devolucion no es valido.");
            }
            if (orderItem.getProduct() instanceof PhysicalProduct) {
                inventoryService.returnForProduct(admin, orderItem.getProduct().getId(), item.getQuantity());
            }
            BigDecimal unitPrice = orderItem.getUnitPrice() == null ? BigDecimal.ZERO : orderItem.getUnitPrice();
            refundAmount = refundAmount.add(unitPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        ReturnRequest saved = returnRequestRepositoryPort.save(returnRequest);

        Refund refund = new Refund();
        refund.setId(UUID.randomUUID().toString());
        refund.setReturnRequest(saved);
        refund.setAmount(refundAmount);
        refund.setState(RefundState.PENDING);
        refundRepositoryPort.save(refund);

        registerAuditLogService.register(admin, "RETURN_RECEIVED", "RETURN", saved.getId(),
                Map.of("refundAmount", refundAmount));
        return saved;
    }

    public List<ReturnRequest> consultMyReturns(User user) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        return returnRequestRepositoryPort.findByBuyerId(user.getId());
    }

    public List<ReturnRequest> consultAll(User user) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SUPERVISOR);
        return returnRequestRepositoryPort.findAll();
    }

    private ReturnRequest load(String returnId) {
        return returnRequestRepositoryPort.findById(returnId)
                .orElseThrow(() -> new EntityNotFoundException("Devolucion no encontrada."));
    }
}
