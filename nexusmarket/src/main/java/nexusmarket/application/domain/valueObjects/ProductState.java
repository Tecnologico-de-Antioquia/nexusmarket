package nexusmarket.application.domain.valueObjects;

public final class ProductState {
    private final String code;
    private final String name;
    private final String description;


    private ProductState(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public static final ProductState PUBLISHED = new ProductState("PUBLISHED", "Publicado", "Producto publicado.");
    public static final ProductState SUSPENDED = new ProductState("SUSPENDED", "Suspendido", "Producto suspendido.");
    public static final ProductState DISCONTINUED = new ProductState("DISCONTINUED", "Descontinuado", "Producto descontinuado.");

}
