package nexusmarket.application.domain.valueobjects;

import java.util.List;

public final class OrderState extends DomainCatalog {

    public static final OrderState CART = new OrderState("CART", "Carrito", "Seleccion provisional de productos.");
    public static final OrderState PENDING_PAYMENT = new OrderState("PENDING_PAYMENT", "Pendiente de pago", "Espera de confirmacion financiera.");
    public static final OrderState PAID = new OrderState("PAID", "Pagado", "Inicio de procesos de alistamiento.");
    public static final OrderState DISPATCHED = new OrderState("DISPATCHED", "Despachado", "Salida fisica de la bodega.");
    public static final OrderState DELIVERED = new OrderState("DELIVERED", "Entregado / Finalizado", "Conclusion satisfactoria de la entrega.");
    public static final OrderState CANCELLED = new OrderState("CANCELLED", "Cancelado", "Pedido cancelado antes del despacho.");

    private OrderState(String code, String name, String description) {
        super(code, name, description);
    }

    public static List<OrderState> values() {
        return List.of(CART, PENDING_PAYMENT, PAID, DISPATCHED, DELIVERED, CANCELLED);
    }

    public static OrderState fromCode(String code) {
        return values().stream()
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown OrderState code: " + code));
    }
}
