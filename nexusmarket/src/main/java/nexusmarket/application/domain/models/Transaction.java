package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.exceptions.InvalidPaymentStateException;
import nexusmarket.application.domain.exceptions.InvalidTransactionException;
import nexusmarket.application.domain.valueobjects.Currency;
import nexusmarket.application.domain.valueobjects.Money;
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
        if (!PaymentState.PENDING.equals(this.state)) {
            throw new InvalidPaymentStateException("Solo una transaccion pendiente puede autorizarse.");
        }
        this.state = PaymentState.AUTHORIZED;
        this.providerReference = providerReference;
    }

    public void markPaid() {
        if (!PaymentState.AUTHORIZED.equals(this.state)) {
            throw new InvalidPaymentStateException("Solo una transaccion autorizada puede marcarse como pagada.");
        }
        this.state = PaymentState.PAID;
    }

    public void fail() {
        if (PaymentState.PAID.equals(this.state) || PaymentState.REFUNDED.equals(this.state)) {
            throw new InvalidPaymentStateException("Una transaccion pagada o reembolsada no puede fallar.");
        }
        this.state = PaymentState.FAILED;
    }

    public void refund() {
        if (!PaymentState.PAID.equals(this.state)) {
            throw new InvalidPaymentStateException("Solo una transaccion pagada puede reembolsarse.");
        }
        this.state = PaymentState.REFUNDED;
    }

    public Money amountAsMoney(Currency currency) {
        if (amount == null) {
            throw new InvalidTransactionException("El monto de la transaccion es obligatorio.");
        }
        return Money.of(amount, currency);
    }
}
