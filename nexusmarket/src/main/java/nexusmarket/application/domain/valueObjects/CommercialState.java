package nexusmarket.application.domain.valueobjects;

import java.util.List;

public final class CommercialState extends DomainCatalog {

    public static final CommercialState ACTIVE = new CommercialState("ACTIVE", "Activo", "El comprador puede realizar compras.");
    public static final CommercialState INACTIVE = new CommercialState("INACTIVE", "Inactivo", "El comprador no puede realizar compras.");

    private CommercialState(String code, String name, String description) {
        super(code, name, description);
    }

    public static List<CommercialState> values() {
        return List.of(ACTIVE, INACTIVE);
    }

    public static CommercialState fromCode(String code) {
        return values().stream()
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown CommercialState code: " + code));
    }
}
