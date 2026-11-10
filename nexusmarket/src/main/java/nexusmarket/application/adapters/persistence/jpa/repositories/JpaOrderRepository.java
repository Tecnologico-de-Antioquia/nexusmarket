package nexusmarket.application.adapters.persistence.jpa.repositories;

import nexusmarket.application.adapters.persistence.jpa.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOrderRepository extends JpaRepository<OrderEntity, String> {

    List<OrderEntity> findByBuyerId(String buyerId);
}
