package nexusmarket.application.adapters.persistence.jpa.repositories;

import nexusmarket.application.adapters.persistence.jpa.entities.ReturnRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaReturnRequestRepository extends JpaRepository<ReturnRequestEntity, String> {

    Optional<ReturnRequestEntity> findByOrderId(String orderId);

    List<ReturnRequestEntity> findByBuyerId(String buyerId);
}
