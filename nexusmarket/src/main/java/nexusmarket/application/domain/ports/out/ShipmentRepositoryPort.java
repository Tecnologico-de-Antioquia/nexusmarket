package nexusmarket.application.domain.ports.out;

import nexusmarket.application.domain.models.Shipment;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepositoryPort {

    Shipment save(Shipment shipment);

    Optional<Shipment> findById(String id);

    Optional<Shipment> findByOrderId(String orderId);

    List<Shipment> findAll();
}
