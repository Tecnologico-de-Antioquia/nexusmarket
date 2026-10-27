package nexusmarket.application.domain.models;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nexusmarket.application.domain.valueObjects.TransactionState;

@Getter
@Setter
public class Transaction {
    private String id;
    private String orderId;
    private String buyerId;
    private BigDecimal amount;
    private TransactionState state;

    public Transaction(String id, String orderId, String buyerId, BigDecimal amount) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction id is required");
        }
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order id is required");
        }
        if (buyerId == null || buyerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Buyer id is required");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero");
        }

        this.id = id.trim();
        this.orderId = orderId.trim();
        this.buyerId = buyerId.trim();
        this.amount = amount;
        this.state = TransactionState.PENDING;
    }

    public void confirm() {
        this.state = TransactionState.PAID;
    }
}
