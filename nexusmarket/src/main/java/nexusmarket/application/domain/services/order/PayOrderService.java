package nexusmarket.application.domain.services.order;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.DomainException;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.InvalidStateTransitionException;
import nexusmarket.application.domain.exceptions.UnauthorizedOperationException;
import nexusmarket.application.domain.models.Invoice;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.PhysicalProduct;
import nexusmarket.application.domain.models.Shipment;
import nexusmarket.application.domain.models.Transaction;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.InvoiceRepositoryPort;
import nexusmarket.application.domain.ports.out.NotificationPort;
import nexusmarket.application.domain.ports.out.OrderRepositoryPort;
import nexusmarket.application.domain.ports.out.PaymentGatewayPort;
import nexusmarket.application.domain.ports.out.TransactionRepositoryPort;
import nexusmarket.application.domain.services.operation.RegisterAuditLogService;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.services.shipment.ShipmentService;
import nexusmarket.application.domain.valueobjects.InvoiceState;
import nexusmarket.application.domain.valueobjects.OrderState;
import nexusmarket.application.domain.valueobjects.PaymentState;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayOrderService {

    private final OrderRepositoryPort orderRepositoryPort;
    private final TransactionRepositoryPort transactionRepositoryPort;
    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final PaymentGatewayPort paymentGatewayPort;
    private final NotificationPort notificationPort;
    private final ShipmentService shipmentService;
    private final RegisterAuditLogService registerAuditLogService;
    private final AuthorizationService authorizationService;

    public Order pay(User user, String orderId) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado."));
        if (order.getBuyer() == null || !order.getBuyer().getId().equals(user.getId())) {
            throw new UnauthorizedOperationException("El pedido no pertenece al comprador.");
        }
        if (!OrderState.PENDING_PAYMENT.equals(order.getState())) {
            throw new InvalidStateTransitionException("El pedido no esta pendiente de pago.");
        }
        if (!paymentGatewayPort.authorize(orderId, order.getTotalAmount())) {
            throw new DomainException("El pago fue rechazado.");
        }
        paymentGatewayPort.capture(orderId, order.getTotalAmount());
        order.pay();

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setOrder(order);
        transaction.setAmount(order.getTotalAmount());
        transaction.setState(PaymentState.PENDING);
        transaction.authorize("AUTH-" + orderId);
        transaction.markPaid();
        transactionRepositoryPort.save(transaction);
        order.setTransaction(transaction);

        Invoice invoice = new Invoice();
        invoice.setId(UUID.randomUUID().toString());
        invoice.setOrder(order);
        invoice.setNumber("INV-" + orderId);
        invoice.setTotal(order.getTotalAmount());
        invoice.setState(InvoiceState.PAID);
        invoiceRepositoryPort.save(invoice);
        order.setInvoice(invoice);

        if (hasPhysicalItems(order)) {
            Shipment shipment = shipmentService.createForOrder(user, order);
            order.setShipment(shipment);
        }

        Order saved = orderRepositoryPort.save(order);
        notificationPort.send(saved.getBuyer().getEmail(), "Pedido pagado",
                "El pedido " + saved.getId() + " fue pagado correctamente.");
        registerAuditLogService.register(user, "ORDER_PAID", "ORDER", saved.getId(),
                Map.of("total", saved.getTotalAmount()));
        return saved;
    }

    private boolean hasPhysicalItems(Order order) {
        return order.getItems().stream().anyMatch(item -> item.getProduct() instanceof PhysicalProduct);
    }
}
