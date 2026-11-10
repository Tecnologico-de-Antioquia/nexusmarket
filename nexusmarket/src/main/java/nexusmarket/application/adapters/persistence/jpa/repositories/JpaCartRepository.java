package nexusmarket.application.adapters.persistence.jpa.repositories;

import nexusmarket.application.adapters.persistence.jpa.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaCartRepository extends JpaRepository<CartEntity, String> {

    Optional<CartEntity> findByBuyerIdAndState(String buyerId, String state);
}
