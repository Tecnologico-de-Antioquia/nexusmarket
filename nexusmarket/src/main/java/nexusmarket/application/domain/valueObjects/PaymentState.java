package nexusmarket.application.domain.valueobjects;

import java.util.List;

public final class PaymentState extends DomainCatalog {

    public static final PaymentState PENDING = new PaymentState("PENDING", "Pendiente", "Pago pendiente de autorizacion.");
    public static final PaymentState AUTHORIZED = new PaymentState("AUTHORIZED", "Autorizado", "Pago autorizado por la pasarela.");
    public static final PaymentState PAID = new PaymentState("PAID", "Pagado", "Pago confirmado.");
    public static final PaymentState FAILED = new PaymentState("FAILED", "Fallido", "Pago rechazado.");
    public static final PaymentState REFUNDED = new PaymentState("REFUNDED", "Reembolsado", "Pago devuelto al comprador.");

    private PaymentState(String code, String name, String description) {
        super(code, name, description);
    }

    public static List<PaymentState> values() {
        return List.of(PENDING, AUTHORIZED, PAID, FAILED, REFUNDED);
    }

    public static PaymentState fromCode(String code) {
        return values().stream()
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown PaymentState code: " + code));
    }
}
