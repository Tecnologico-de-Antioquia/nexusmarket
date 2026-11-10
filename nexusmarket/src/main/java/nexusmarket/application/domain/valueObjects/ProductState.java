package nexusmarket.application.domain.valueobjects;

import java.util.List;

public final class ProductState extends DomainCatalog {

    public static final ProductState PUBLISHED = new ProductState("PUBLISHED", "Publicado", "El producto es visible en el catalogo publico.");
    public static final ProductState SUSPENDED = new ProductState("SUSPENDED", "Suspendido", "El producto no puede comercializarse temporalmente.");
    public static final ProductState DISCONTINUED = new ProductState("DISCONTINUED", "Descontinuado", "El producto no volvera a comercializarse.");

    private ProductState(String code, String name, String description) {
        super(code, name, description);
    }

    public static List<ProductState> values() {
        return List.of(PUBLISHED, SUSPENDED, DISCONTINUED);
    }

    public static ProductState fromCode(String code) {
        return values().stream()
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ProductState code: " + code));
    }
}
