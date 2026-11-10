package nexusmarket.application.adapters.persistence.jpa.adapters;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.persistence.jpa.entities.OrderEntity;
import nexusmarket.application.adapters.persistence.jpa.mappers.JpaMapper;
import nexusmarket.application.adapters.persistence.jpa.repositories.JpaOrderRepository;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.ReturnRequest;
import nexusmarket.application.domain.ports.out.InvoiceRepositoryPort;
import nexusmarket.application.domain.ports.out.OrderRepositoryPort;
import nexusmarket.application.domain.ports.out.ShipmentRepositoryPort;
import nexusmarket.application.domain.ports.out.TransactionRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaOrderRepositoryAdapter implements OrderRepositoryPort {

    private final JpaOrderRepository repository;
    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final TransactionRepositoryPort transactionRepositoryPort;
    private final ShipmentRepositoryPort shipmentRepositoryPort;

    @Override
    public Order save(Order order) {
        OrderEntity entity = repository.save(JpaMapper.toEntity(order));
        order.setId(entity.getId());
        return order;
    }

    @Override
    public Optional<Order> findById(String id) {
        return repository.findById(id).map(this::enrich);
    }

    @Override
    public List<Order> findAll() {
        return repository.findAll().stream().map(this::enrich).toList();
    }

    @Override
    public List<Order> findByBuyerId(String buyerId) {
        return repository.findByBuyerId(buyerId).stream().map(this::enrich).toList();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    private Order enrich(OrderEntity entity) {
        Order order = JpaMapper.toDomain(entity);
        if (order == null) {
            return null;
        }
        if (entity.getInvoiceId() != null) {
            invoiceRepositoryPort.findById(entity.getInvoiceId()).ifPresent(order::setInvoice);
        }
        if (entity.getTransactionId() != null) {
            transactionRepositoryPort.findById(entity.getTransactionId()).ifPresent(order::setTransaction);
        }
        if (entity.getShipmentId() != null) {
            shipmentRepositoryPort.findById(entity.getShipmentId()).ifPresent(order::setShipment);
        }
        if (entity.getReturnRequestId() != null) {
            ReturnRequest returnRequest = new ReturnRequest();
            returnRequest.setId(entity.getReturnRequestId());
            order.setReturnRequest(returnRequest);
        }
        return order;
    }
}
