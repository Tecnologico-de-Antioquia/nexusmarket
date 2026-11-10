package nexusmarket.application.adapters.persistence.jpa.repositories;

import nexusmarket.application.adapters.persistence.jpa.entities.RefundEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRefundRepository extends JpaRepository<RefundEntity, String> {

    Optional<RefundEntity> findByReturnId(String returnId);
}
