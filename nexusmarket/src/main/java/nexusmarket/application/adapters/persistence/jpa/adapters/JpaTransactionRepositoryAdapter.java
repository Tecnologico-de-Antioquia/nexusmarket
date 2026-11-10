package nexusmarket.application.adapters.persistence.jpa.adapters;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.persistence.jpa.entities.TransactionEntity;
import nexusmarket.application.adapters.persistence.jpa.mappers.JpaMapper;
import nexusmarket.application.adapters.persistence.jpa.repositories.JpaTransactionRepository;
import nexusmarket.application.domain.models.Transaction;
import nexusmarket.application.domain.ports.out.TransactionRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaTransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final JpaTransactionRepository repository;

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = repository.save(JpaMapper.toEntity(transaction));
        transaction.setId(entity.getId());
        return transaction;
    }

    @Override
    public Optional<Transaction> findById(String id) {
        return repository.findById(id).map(JpaMapper::toDomain);
    }

    @Override
    public Optional<Transaction> findByOrderId(String orderId) {
        return repository.findByOrderId(orderId).map(JpaMapper::toDomain);
    }

    @Override
    public List<Transaction> findAll() {
        return repository.findAll().stream().map(JpaMapper::toDomain).toList();
    }
}
