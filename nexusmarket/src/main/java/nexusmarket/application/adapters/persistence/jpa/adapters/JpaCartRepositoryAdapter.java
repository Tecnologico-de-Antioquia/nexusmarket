package nexusmarket.application.adapters.persistence.jpa.adapters;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.persistence.jpa.entities.CartEntity;
import nexusmarket.application.adapters.persistence.jpa.mappers.JpaMapper;
import nexusmarket.application.adapters.persistence.jpa.repositories.JpaCartRepository;
import nexusmarket.application.domain.models.Cart;
import nexusmarket.application.domain.models.CartItem;
import nexusmarket.application.domain.ports.out.BuyerRepositoryPort;
import nexusmarket.application.domain.ports.out.CartRepositoryPort;
import nexusmarket.application.domain.ports.out.ProductRepositoryPort;
import nexusmarket.application.domain.valueobjects.CartState;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaCartRepositoryAdapter implements CartRepositoryPort {

    private final JpaCartRepository repository;
    private final ProductRepositoryPort productRepositoryPort;
    private final BuyerRepositoryPort buyerRepositoryPort;

    @Override
    public Cart save(Cart cart) {
        CartEntity entity = repository.save(JpaMapper.toEntity(cart));
        cart.setId(entity.getId());
        return cart;
    }

    @Override
    public Optional<Cart> findById(String id) {
        return repository.findById(id).map(this::enrich);
    }

    @Override
    public Optional<Cart> findActiveByBuyerId(String buyerId) {
        return repository.findByBuyerIdAndState(buyerId, CartState.ACTIVE.getCode()).map(this::enrich);
    }

    @Override
    public List<Cart> findAll() {
        return repository.findAll().stream().map(this::enrich).toList();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    private Cart enrich(CartEntity entity) {
        Cart cart = JpaMapper.toDomain(entity);
        if (cart != null && cart.getBuyer() != null) {
            buyerRepositoryPort.findById(cart.getBuyer().getId()).ifPresent(cart::setBuyer);
        }
        if (cart != null && cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                if (item.getProduct() != null) {
                    productRepositoryPort.findById(item.getProduct().getId()).ifPresent(item::setProduct);
                }
            }
        }
        return cart;
    }
}
