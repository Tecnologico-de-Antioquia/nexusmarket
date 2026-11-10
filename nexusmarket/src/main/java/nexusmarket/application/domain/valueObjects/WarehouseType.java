package nexusmarket.application.domain.valueobjects;

import java.util.List;

public final class WarehouseType extends DomainCatalog {

    public static final WarehouseType MARKETPLACE = new WarehouseType("MARKETPLACE", "Marketplace", "Bodega administrada por el marketplace.");
    public static final WarehouseType SELLER = new WarehouseType("SELLER", "Vendedor", "Bodega administrada por un vendedor.");

    private WarehouseType(String code, String name, String description) {
        super(code, name, description);
    }

    public static List<WarehouseType> values() {
        return List.of(MARKETPLACE, SELLER);
    }

    public static WarehouseType fromCode(String code) {
        return values().stream()
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown WarehouseType code: " + code));
    }
}
