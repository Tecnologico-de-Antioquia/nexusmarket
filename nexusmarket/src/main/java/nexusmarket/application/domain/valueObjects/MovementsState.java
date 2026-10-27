package nexusmarket.application.domain.valueObjects;

public final class MovementsState {
    private final String code;
    private final String name;
    private final String description;

    private MovementsState(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public static final MovementsState RECEIPT = new MovementsState("RECEIPT", "Recibido", "Inventory entry");
    public static final MovementsState RESERVATION = new MovementsState("RESERVATION", "Reservado", "Reserved inventory");
    public static final MovementsState SALE_OUT = new MovementsState("SALE_OUT", "Salida", "Inventory sale");
    public static final MovementsState ADJUSMENT = new MovementsState("ADJUSMENT", "Ajuste", "Inventory adjustment");
    public static final MovementsState RETURN = new MovementsState("RETURN", "Devolución", "Return to stock");

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
