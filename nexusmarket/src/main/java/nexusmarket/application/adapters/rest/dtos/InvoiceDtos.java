package nexusmarket.application.adapters.rest.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class InvoiceDtos {

    private InvoiceDtos() {
    }

    public record InvoiceResponse(String id, String orderId, String number, BigDecimal total, String state,
                                  LocalDateTime issuedAt) {
    }
}
