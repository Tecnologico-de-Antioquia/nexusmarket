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

    public static final MovementsState RECEIPT = new MovementsState("RECEIPT", "Recibido", "Recibido.");
    public static final MovementsState RESERVATION = new MovementsState("RESERVATION", "Reservado", "Reservado.");
    public static final MovementsState SALE_OUT = new MovementsState("SALE_OUT", "Sale out", "Salida.");
    public static final MovementsState ADJUSMENT = new MovementsState("ADJUSMENT", "Adjustment", "Ajuste.");
    public static final MovementsState RETURN = new MovementsState("RETURN", "Retornado", "Retornado.");

}
