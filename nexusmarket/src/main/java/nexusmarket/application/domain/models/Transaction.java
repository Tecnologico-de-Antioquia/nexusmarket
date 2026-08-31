package nexusmarket.application.domain.models;

import nexusmarket.application.domain.valueObjects.TransactionState;

public class Transaction {
    private Integer id;
    private Integer idBuyer;
    private TransactionState state;
}
