package nexusmarket.application.domain.valueObjects;

public final class WarehouseType {
    private final String code;
    private final String name;
    private final String description;


    private WarehouseType(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public static final WarehouseType MARKETPLACE = new WarehouseType("MARKETPLACE", "Marketplace", "Bodega propia del marketplace.");
    public static final WarehouseType SELLER = new WarehouseType("SELLER", "Vendedor", "Bodega propia del proveedor.");

}
