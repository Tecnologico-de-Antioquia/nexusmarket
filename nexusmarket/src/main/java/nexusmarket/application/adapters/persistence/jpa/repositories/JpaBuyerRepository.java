package nexusmarket.application.adapters.persistence.jpa.repositories;

import nexusmarket.application.adapters.persistence.jpa.entities.BuyerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaBuyerRepository extends JpaRepository<BuyerEntity, String> {

    Optional<BuyerEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
