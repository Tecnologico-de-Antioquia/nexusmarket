package nexusmarket.application.domain.ports.out;

import nexusmarket.application.domain.models.Cart;

import java.util.List;
import java.util.Optional;

public interface CartRepositoryPort {

    Cart save(Cart cart);

    Optional<Cart> findById(String id);

    Optional<Cart> findActiveByBuyerId(String buyerId);

    List<Cart> findAll();

    void delete(String id);
}
