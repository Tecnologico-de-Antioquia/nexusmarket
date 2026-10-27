package nexusmarket.application.domain.ports.out;

import java.util.List;
import java.util.Optional;

import nexusmarket.application.domain.models.Order;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(String id);
    List<Order> findAll();
    List<Order> findByBuyerId(String buyerId);
    void delete(String id);
}
