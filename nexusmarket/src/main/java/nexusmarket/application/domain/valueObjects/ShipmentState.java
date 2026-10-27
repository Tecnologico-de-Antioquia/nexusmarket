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

    public static final ShipmentState PENDING = new ShipmentState("PENDING", "Pendiente", "Shipment awaiting dispatch");
    public static final ShipmentState DISPATCHED = new ShipmentState("DISPATCHED", "Despachado", "Shipment dispatched");
    public static final ShipmentState DELIVERED = new ShipmentState("DELIVERED", "Entregado", "Shipment delivered");

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
