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

    public static final TransactionState PENDING = new TransactionState("PENDING", "Pendiente", "Waiting for payment");
    public static final TransactionState PAID = new TransactionState("PAID", "Pagado", "Payment confirmed");
    public static final TransactionState FAILED = new TransactionState("FAILED", "Fallida", "Payment failed");

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
