package nexusmarket.application.adapters.persistence.jpa.repositories;

import nexusmarket.application.adapters.persistence.jpa.entities.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaInvoiceRepository extends JpaRepository<InvoiceEntity, String> {

    Optional<InvoiceEntity> findByOrderId(String orderId);
}
