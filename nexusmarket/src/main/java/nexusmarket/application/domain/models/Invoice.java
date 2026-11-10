package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.exceptions.InvalidInvoiceStateException;
import nexusmarket.application.domain.valueobjects.Currency;
import nexusmarket.application.domain.valueobjects.InvoiceState;
import nexusmarket.application.domain.valueobjects.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Invoice {

    private String id;
    private Order order;
    private String number;
    private LocalDateTime issuedAt = LocalDateTime.now();
    private BigDecimal total;
    private InvoiceState state = InvoiceState.ISSUED;

    public void markPaid() {
        if (!InvoiceState.ISSUED.equals(this.state)) {
            throw new InvalidInvoiceStateException("Solo una factura emitida puede marcarse como pagada.");
        }
        this.state = InvoiceState.PAID;
    }

    public void voidInvoice() {
        if (!InvoiceState.ISSUED.equals(this.state)) {
            throw new InvalidInvoiceStateException("Solo una factura emitida puede anularse.");
        }
        this.state = InvoiceState.VOID;
    }

    public Money totalAsMoney(Currency currency) {
        return Money.of(total == null ? BigDecimal.ZERO : total, currency);
    }
}
