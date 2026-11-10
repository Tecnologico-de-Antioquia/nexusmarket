package nexusmarket.application.domain.ports.out;

import nexusmarket.application.domain.models.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepositoryPort {

    Transaction save(Transaction transaction);

    Optional<Transaction> findById(String id);

    Optional<Transaction> findByOrderId(String orderId);

    List<Transaction> findAll();
}
