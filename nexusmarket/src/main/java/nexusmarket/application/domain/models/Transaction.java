package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.valueobjects.PaymentState;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    private String id;
    private Order order;
    private BigDecimal amount;
    private PaymentState state = PaymentState.PENDING;
    private String providerReference;

    public void authorize(String providerReference) {
        this.state = PaymentState.AUTHORIZED;
        this.providerReference = providerReference;
    }

    public void markPaid() {
        this.state = PaymentState.PAID;
    }

    public void fail() {
        this.state = PaymentState.FAILED;
    }

    public void refund() {
        this.state = PaymentState.REFUNDED;
    }
}
