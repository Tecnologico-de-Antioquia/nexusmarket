package nexusmarket.application.domain.valueobjects;

import java.util.List;

public final class InventoryCondition extends DomainCatalog {

    public static final InventoryCondition AVAILABLE = new InventoryCondition("AVAILABLE", "Disponible", "Inventario apto para reserva y venta.");
    public static final InventoryCondition DAMAGED = new InventoryCondition("DAMAGED", "Danado", "Inventario no disponible para reserva ni venta.");

    private InventoryCondition(String code, String name, String description) {
        super(code, name, description);
    }

    public static List<InventoryCondition> values() {
        return List.of(AVAILABLE, DAMAGED);
    }

    public static InventoryCondition fromCode(String code) {
        return values().stream()
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown InventoryCondition code: " + code));
    }
}
