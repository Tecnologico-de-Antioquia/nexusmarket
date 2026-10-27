package nexusmarket.application.domain.ports.out;

import java.util.Optional;

import nexusmarket.application.domain.models.Buyer;

public interface BuyerRepositoryPort {
    Buyer save(Buyer buyer);
    Optional<Buyer> findById(String id);
    Optional<Buyer> findByEmail(String email);
    boolean existsByEmail(String email);
    void delete(String id);
}
