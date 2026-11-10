package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.exceptions.InvalidRefundException;
import nexusmarket.application.domain.exceptions.InvalidRefundStateException;
import nexusmarket.application.domain.valueobjects.Currency;
import nexusmarket.application.domain.valueobjects.Money;
import nexusmarket.application.domain.valueobjects.RefundState;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Refund {

    private String id;
    private ReturnRequest returnRequest;
    private BigDecimal amount;
    private RefundState state = RefundState.PENDING;
    private LocalDateTime processedAt;

    public void process() {
        if (!RefundState.PENDING.equals(this.state)) {
            throw new InvalidRefundStateException("Solo un reembolso pendiente puede procesarse.");
        }
        this.state = RefundState.PROCESSED;
        this.processedAt = LocalDateTime.now();
    }

    public void fail() {
        if (!RefundState.PENDING.equals(this.state)) {
            throw new InvalidRefundStateException("Solo un reembolso pendiente puede fallar.");
        }
        this.state = RefundState.FAILED;
        this.processedAt = LocalDateTime.now();
    }

    public Money amountAsMoney(Currency currency) {
        if (amount == null) {
            throw new InvalidRefundException("El monto del reembolso es obligatorio.");
        }
        return Money.of(amount, currency);
    }
}
