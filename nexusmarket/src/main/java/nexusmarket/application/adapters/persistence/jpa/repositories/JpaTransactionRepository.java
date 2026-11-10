package nexusmarket.application.adapters.persistence.jpa.repositories;

import nexusmarket.application.adapters.persistence.jpa.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, String> {

    Optional<TransactionEntity> findByOrderId(String orderId);
}
