package nexusmarket.application.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.ports.out.OrderRepositoryPort;

@Repository
public class InMemoryOrderRepository implements OrderRepositoryPort {
    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    @Override
    public Order save(Order order) {
        orders.put(order.getId(), order);
        return order;
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public List<Order> findByBuyerId(String buyerId) {
        return orders.values().stream()
                .filter(order -> order.getBuyerId().equals(buyerId))
                .toList();
    }

    @Override
    public void delete(String id) {
        orders.remove(id);
    }
}
