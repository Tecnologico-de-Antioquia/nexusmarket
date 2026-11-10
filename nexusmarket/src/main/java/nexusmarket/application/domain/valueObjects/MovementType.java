package nexusmarket.application.domain.valueobjects;

import java.util.List;

public final class MovementType extends DomainCatalog {

    public static final MovementType RECEIPT = new MovementType("RECEIPT", "Ingreso", "Ingreso de existencias.");
    public static final MovementType RESERVATION = new MovementType("RESERVATION", "Reserva", "Reserva de existencias para una venta.");
    public static final MovementType SALE_OUT = new MovementType("SALE_OUT", "Salida por venta", "Salida de existencias por una venta.");
    public static final MovementType ADJUSTMENT = new MovementType("ADJUSTMENT", "Ajuste", "Ajuste manual de existencias.");
    public static final MovementType RETURN = new MovementType("RETURN", "Devolucion", "Devolucion de existencias al inventario.");

    private MovementType(String code, String name, String description) {
        super(code, name, description);
    }

    public static List<MovementType> values() {
        return List.of(RECEIPT, RESERVATION, SALE_OUT, ADJUSTMENT, RETURN);
    }

    public static MovementType fromCode(String code) {
        return values().stream()
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown MovementType code: " + code));
    }
}
