package nexusmarket.application.domain.ports.out;

import nexusmarket.application.domain.models.Buyer;

import java.util.List;
import java.util.Optional;

public interface BuyerRepositoryPort {

    Buyer save(Buyer buyer);

    Optional<Buyer> findById(String id);

    Optional<Buyer> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Buyer> findAll();

    void delete(String id);
}
