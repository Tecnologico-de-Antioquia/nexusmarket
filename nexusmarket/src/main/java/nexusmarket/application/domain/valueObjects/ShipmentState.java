package nexusmarket.application.domain.valueObjects;

public final class ShipmentState {
    private final String code;
    private final String name;
    private final String description;


    private ShipmentState(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public static final ShipmentState DISPATCHED = new ShipmentState("DISPATCHED", "Despachado", "Despachado.");
    public static final ShipmentState DELIVERED = new ShipmentState("DELIVERED", "Entregado", "Entregado.");

}
