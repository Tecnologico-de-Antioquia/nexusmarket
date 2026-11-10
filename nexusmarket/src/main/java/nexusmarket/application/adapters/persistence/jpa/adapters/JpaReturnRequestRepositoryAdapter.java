package nexusmarket.application.adapters.persistence.jpa.adapters;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.persistence.jpa.entities.ReturnRequestEntity;
import nexusmarket.application.adapters.persistence.jpa.mappers.JpaMapper;
import nexusmarket.application.adapters.persistence.jpa.repositories.JpaReturnRequestRepository;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.OrderItem;
import nexusmarket.application.domain.models.ReturnItem;
import nexusmarket.application.domain.models.ReturnRequest;
import nexusmarket.application.domain.ports.out.OrderRepositoryPort;
import nexusmarket.application.domain.ports.out.ReturnRequestRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaReturnRequestRepositoryAdapter implements ReturnRequestRepositoryPort {

    private final JpaReturnRequestRepository repository;
    private final OrderRepositoryPort orderRepositoryPort;

    @Override
    public ReturnRequest save(ReturnRequest returnRequest) {
        ReturnRequestEntity entity = repository.save(JpaMapper.toEntity(returnRequest));
        returnRequest.setId(entity.getId());
        return returnRequest;
    }

    @Override
    public Optional<ReturnRequest> findById(String id) {
        return repository.findById(id).map(this::enrich);
    }

    @Override
    public Optional<ReturnRequest> findByOrderId(String orderId) {
        return repository.findByOrderId(orderId).map(this::enrich);
    }

    @Override
    public List<ReturnRequest> findAll() {
        return repository.findAll().stream().map(this::enrich).toList();
    }

    @Override
    public List<ReturnRequest> findByBuyerId(String buyerId) {
        return repository.findByBuyerId(buyerId).stream().map(this::enrich).toList();
    }

    private ReturnRequest enrich(ReturnRequestEntity entity) {
        ReturnRequest returnRequest = JpaMapper.toDomain(entity);
        if (returnRequest == null || entity.getOrderId() == null) {
            return returnRequest;
        }
        Order order = orderRepositoryPort.findById(entity.getOrderId()).orElse(null);
        if (order == null) {
            return returnRequest;
        }
        returnRequest.setOrder(order);
        for (ReturnItem item : returnRequest.getItems()) {
            if (item.getOrderItem() == null || item.getOrderItem().getId() == null) {
                continue;
            }
            String orderItemId = item.getOrderItem().getId();
            for (OrderItem orderItem : order.getItems()) {
                if (orderItemId.equals(orderItem.getId())) {
                    item.setOrderItem(orderItem);
                    break;
                }
            }
        }
        return returnRequest;
    }
}
