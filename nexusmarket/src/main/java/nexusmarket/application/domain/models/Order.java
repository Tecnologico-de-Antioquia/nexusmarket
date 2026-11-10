package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.exceptions.InvalidStateTransitionException;
import nexusmarket.application.domain.valueobjects.OrderState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Order {

    private String id;
    private Buyer buyer;
    private List<OrderItem> items = new ArrayList<>();
    private OrderState state = OrderState.CART;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime paidAt;
    private LocalDateTime dispatchedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
    private Invoice invoice;
    private Transaction transaction;
    private Shipment shipment;
    private ReturnRequest returnRequest;

    public void recalculateTotal() {
        this.totalAmount = this.items.stream()
                .map(OrderItem::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void submit() {
        if (!OrderState.CART.equals(this.state)) {
            throw new InvalidStateTransitionException("Solo un pedido en carrito puede enviarse a pago.");
        }
        this.state = OrderState.PENDING_PAYMENT;
    }

    public void pay() {
        if (!OrderState.PENDING_PAYMENT.equals(this.state)) {
            throw new InvalidStateTransitionException("Solo un pedido pendiente de pago puede pagarse.");
        }
        this.state = OrderState.PAID;
        this.paidAt = LocalDateTime.now();
    }

    public void dispatch() {
        if (!OrderState.PAID.equals(this.state)) {
            throw new InvalidStateTransitionException("Solo un pedido pagado puede despacharse.");
        }
        this.state = OrderState.DISPATCHED;
        this.dispatchedAt = LocalDateTime.now();
    }

    public void deliver() {
        if (!OrderState.DISPATCHED.equals(this.state)) {
            throw new InvalidStateTransitionException("Solo un pedido despachado puede entregarse.");
        }
        this.state = OrderState.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    public void cancel() {
        if (OrderState.DELIVERED.equals(this.state) || OrderState.CANCELLED.equals(this.state)) {
            throw new InvalidStateTransitionException("Un pedido finalizado o cancelado no puede cancelarse.");
        }
        this.state = OrderState.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }

    public boolean isFinalized() {
        return OrderState.DELIVERED.equals(this.state) || OrderState.CANCELLED.equals(this.state);
    }
}
