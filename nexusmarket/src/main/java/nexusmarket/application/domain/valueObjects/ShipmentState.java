package nexusmarket.application.domain.valueobjects;

import java.util.List;

public final class ShipmentState extends DomainCatalog {

    public static final ShipmentState PENDING = new ShipmentState("PENDING", "Pendiente", "Envio pendiente de despacho.");
    public static final ShipmentState DISPATCHED = new ShipmentState("DISPATCHED", "Despachado", "Envio despachado.");
    public static final ShipmentState DELIVERED = new ShipmentState("DELIVERED", "Entregado", "Envio entregado al comprador.");

    private ShipmentState(String code, String name, String description) {
        super(code, name, description);
    }

    public static List<ShipmentState> values() {
        return List.of(PENDING, DISPATCHED, DELIVERED);
    }

    public static ShipmentState fromCode(String code) {
        return values().stream()
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ShipmentState code: " + code));
    }
}
