package nexusmarket.application.domain.valueobjects;

import java.util.List;

public final class ReturnState extends DomainCatalog {

    public static final ReturnState REQUESTED = new ReturnState("REQUESTED", "Solicitada", "Devolucion solicitada por el comprador.");
    public static final ReturnState APPROVED = new ReturnState("APPROVED", "Aprobada", "Devolucion aprobada por el administrador.");
    public static final ReturnState REJECTED = new ReturnState("REJECTED", "Rechazada", "Devolucion rechazada por el administrador.");
    public static final ReturnState RECEIVED = new ReturnState("RECEIVED", "Recibida", "Devolucion recibida e ingresada al inventario.");
    public static final ReturnState COMPLETED = new ReturnState("COMPLETED", "Completada", "Devolucion finalizada.");

    private ReturnState(String code, String name, String description) {
        super(code, name, description);
    }

    public static List<ReturnState> values() {
        return List.of(REQUESTED, APPROVED, REJECTED, RECEIVED, COMPLETED);
    }

    public static ReturnState fromCode(String code) {
        return values().stream()
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ReturnState code: " + code));
    }
}
