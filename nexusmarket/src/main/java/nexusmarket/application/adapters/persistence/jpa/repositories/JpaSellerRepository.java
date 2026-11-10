package nexusmarket.application.adapters.persistence.jpa.repositories;

import nexusmarket.application.adapters.persistence.jpa.entities.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaSellerRepository extends JpaRepository<SellerEntity, String> {

    Optional<SellerEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
