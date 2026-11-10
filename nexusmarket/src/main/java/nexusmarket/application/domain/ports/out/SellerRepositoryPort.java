package nexusmarket.application.domain.ports.out;

import nexusmarket.application.domain.models.Seller;

import java.util.List;
import java.util.Optional;

public interface SellerRepositoryPort {

    Seller save(Seller seller);

    Optional<Seller> findById(String id);

    Optional<Seller> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Seller> findAll();

    void delete(String id);
}
