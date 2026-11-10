package nexusmarket.application.domain.valueobjects;

import java.util.List;

public final class InvoiceState extends DomainCatalog {

    public static final InvoiceState ISSUED = new InvoiceState("ISSUED", "Emitida", "Factura emitida y vigente.");
    public static final InvoiceState PAID = new InvoiceState("PAID", "Pagada", "Factura pagada.");
    public static final InvoiceState VOID = new InvoiceState("VOID", "Anulada", "Factura anulada.");

    private InvoiceState(String code, String name, String description) {
        super(code, name, description);
    }

    public static List<InvoiceState> values() {
        return List.of(ISSUED, PAID, VOID);
    }

    public static InvoiceState fromCode(String code) {
        return values().stream()
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown InvoiceState code: " + code));
    }
}
