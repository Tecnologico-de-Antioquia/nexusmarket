package nexusmarket.application.domain.valueobjects;

import java.util.List;

public final class CartState extends DomainCatalog {

    public static final CartState ACTIVE = new CartState("ACTIVE", "Activo", "Carrito en uso.");
    public static final CartState CHECKED_OUT = new CartState("CHECKED_OUT", "Convertido en pedido", "Carrito convertido en pedido.");
    public static final CartState ABANDONED = new CartState("ABANDONED", "Abandonado", "Carrito abandonado por el comprador.");

    private CartState(String code, String name, String description) {
        super(code, name, description);
    }

    public static List<CartState> values() {
        return List.of(ACTIVE, CHECKED_OUT, ABANDONED);
    }

    public static CartState fromCode(String code) {
        return values().stream()
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown CartState code: " + code));
    }
}
