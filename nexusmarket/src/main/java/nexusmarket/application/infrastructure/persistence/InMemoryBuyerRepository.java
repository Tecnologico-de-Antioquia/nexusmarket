package nexusmarket.application.infrastructure.persistence;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.ports.out.BuyerRepositoryPort;

@Repository
public class InMemoryBuyerRepository implements BuyerRepositoryPort {
    private final Map<String, Buyer> buyers = new ConcurrentHashMap<>();

    @Override
    public Buyer save(Buyer buyer) {
        buyers.put(buyer.getId(), buyer);
        return buyer;
    }

    @Override
    public Optional<Buyer> findById(String id) {
        return Optional.ofNullable(buyers.get(id));
    }

    @Override
    public Optional<Buyer> findByEmail(String email) {
        return buyers.values().stream()
                .filter(buyer -> buyer.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        return buyers.values().stream().anyMatch(buyer -> buyer.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public void delete(String id) {
        buyers.remove(id);
    }
}
