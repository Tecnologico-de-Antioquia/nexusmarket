package nexusmarket.application.domain.valueObjects;

public final class OrderState {
    private final String code;
    private final String name;
    private final String description;


    private OrderState(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public static final OrderState CANCELLED = new OrderState("CANCELLED", "Cancelado", "Cancelada.");
    public static final OrderState COMPLETED = new OrderState("COMPLETED", "Completado", "Completada.");

}
