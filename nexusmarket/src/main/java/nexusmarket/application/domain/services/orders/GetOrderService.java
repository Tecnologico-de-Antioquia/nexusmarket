package nexusmarket.application.domain.services.orders;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.DomainException;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.ports.out.OrderRepositoryPort;

@Service
@RequiredArgsConstructor
public class GetOrderService {

    private final OrderRepositoryPort orderRepositoryPort;

    public Order findById(String orderId) {
        return orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new DomainException("Order was not found"));
    }

    public List<Order> findByBuyerId(String buyerId) {
        return orderRepositoryPort.findByBuyerId(buyerId);
    }
}
