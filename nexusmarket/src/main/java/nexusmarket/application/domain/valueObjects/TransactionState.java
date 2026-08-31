package nexusmarket.application.domain.valueObjects;

public final class TransactionState {
    private final String code;
    private final String name;
    private final String description;


    private TransactionState(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public static final TransactionState PENDING = new TransactionState("PENDING", "Pendiente", "Pendiente.");
    public static final TransactionState PAID = new TransactionState("PAID", "Pagado", "Pagado.");

}
