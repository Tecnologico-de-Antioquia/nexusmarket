package nexusmarket.application.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nexusmarket.application.domain.valueObjects.OrderState;

@Getter
@Setter
public class Order {
    private String id;
    private String buyerId;
    private List<String> productIds = new ArrayList<>();
    private Transaction transaction;
    private Shipment shipment;
    private OrderState state;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    public Order(String id, String buyerId, List<String> productIds, BigDecimal totalAmount) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Order id is required");
        }
        if (buyerId == null || buyerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Buyer id is required");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product");
        }
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Order total amount must be greater than zero");
        }

        this.id = id.trim();
        this.buyerId = buyerId.trim();
        this.productIds = new ArrayList<>(productIds);
        this.totalAmount = totalAmount;
        this.state = OrderState.CART;
        this.createdAt = LocalDateTime.now();
    }

    public void pay() {
        if (this.state == OrderState.CANCELLED || this.state == OrderState.COMPLETED) {
            throw new IllegalStateException("Order cannot be paid in its current state");
        }
        this.state = OrderState.PAID;
    }

    public void dispatch() {
        if (this.state != OrderState.PAID) {
            throw new IllegalStateException("Order must be paid before dispatch");
        }
        this.state = OrderState.DISPATCHED;
    }

    public void complete() {
        if (this.state != OrderState.DISPATCHED) {
            throw new IllegalStateException("Order must be dispatched before completion");
        }
        this.state = OrderState.COMPLETED;
    }

    public void cancel() {
        if (this.state == OrderState.COMPLETED) {
            throw new IllegalStateException("A completed order cannot be cancelled");
        }
        this.state = OrderState.CANCELLED;
    }
}
