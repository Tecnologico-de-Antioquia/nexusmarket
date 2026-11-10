package nexusmarket.application.domain.valueobjects;

import java.util.List;

public final class ProductType extends DomainCatalog {

    public static final ProductType PHYSICAL = new ProductType("PHYSICAL", "Fisico", "Producto que requiere inventario y despacho.");
    public static final ProductType DIGITAL = new ProductType("DIGITAL", "Digital", "Producto con entrega inmediata tras el pago.");

    private ProductType(String code, String name, String description) {
        super(code, name, description);
    }

    public static List<ProductType> values() {
        return List.of(PHYSICAL, DIGITAL);
    }

    public static ProductType fromCode(String code) {
        return values().stream()
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ProductType code: " + code));
    }
}
