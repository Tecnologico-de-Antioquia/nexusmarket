package nexusmarket.application.adapters.persistence.jpa.adapters;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.persistence.jpa.entities.ShipmentEntity;
import nexusmarket.application.adapters.persistence.jpa.mappers.JpaMapper;
import nexusmarket.application.adapters.persistence.jpa.repositories.JpaShipmentRepository;
import nexusmarket.application.domain.models.Shipment;
import nexusmarket.application.domain.ports.out.ShipmentRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaShipmentRepositoryAdapter implements ShipmentRepositoryPort {

    private final JpaShipmentRepository repository;

    @Override
    public Shipment save(Shipment shipment) {
        ShipmentEntity entity = repository.save(JpaMapper.toEntity(shipment));
        shipment.setId(entity.getId());
        return shipment;
    }

    @Override
    public Optional<Shipment> findById(String id) {
        return repository.findById(id).map(JpaMapper::toDomain);
    }

    @Override
    public Optional<Shipment> findByOrderId(String orderId) {
        return repository.findByOrderId(orderId).map(JpaMapper::toDomain);
    }

    @Override
    public List<Shipment> findAll() {
        return repository.findAll().stream().map(JpaMapper::toDomain).toList();
    }
}
