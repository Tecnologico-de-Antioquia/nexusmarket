package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.valueobjects.InvoiceState;

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
        this.state = InvoiceState.PAID;
    }

    public void voidInvoice() {
        this.state = InvoiceState.VOID;
    }
}
