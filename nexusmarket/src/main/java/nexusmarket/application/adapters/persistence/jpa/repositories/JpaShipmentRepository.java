package nexusmarket.application.adapters.persistence.jpa.repositories;

import nexusmarket.application.adapters.persistence.jpa.entities.ShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaShipmentRepository extends JpaRepository<ShipmentEntity, String> {

    Optional<ShipmentEntity> findByOrderId(String orderId);
}
