package nexusmarket.application.domain.valueobjects;

import java.util.List;

public final class RefundState extends DomainCatalog {

    public static final RefundState PENDING = new RefundState("PENDING", "Pendiente", "Reembolso pendiente de procesamiento.");
    public static final RefundState PROCESSED = new RefundState("PROCESSED", "Procesado", "Reembolso procesado.");
    public static final RefundState FAILED = new RefundState("FAILED", "Fallido", "Reembolso fallido.");

    private RefundState(String code, String name, String description) {
        super(code, name, description);
    }

    public static List<RefundState> values() {
        return List.of(PENDING, PROCESSED, FAILED);
    }

    public static RefundState fromCode(String code) {
        return values().stream()
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown RefundState code: " + code));
    }
}
