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

    public static final OrderState CART = new OrderState("CART", "Carrito", "Items selected but not paid");
    public static final OrderState PENDING_PAYMENT = new OrderState("PENDING_PAYMENT", "Pendiente de pago", "Waiting for payment confirmation");
    public static final OrderState PAID = new OrderState("PAID", "Pagado", "Payment confirmed");
    public static final OrderState DISPATCHED = new OrderState("DISPATCHED", "Despachado", "Order dispatched from warehouse");
    public static final OrderState DELIVERED = new OrderState("DELIVERED", "Entregado", "Order delivered to buyer");
    public static final OrderState COMPLETED = new OrderState("COMPLETED", "Completado", "Order closed successfully");
    public static final OrderState CANCELLED = new OrderState("CANCELLED", "Cancelado", "Order cancelled");

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
