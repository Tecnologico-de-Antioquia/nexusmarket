package nexusmarket.application.domain.services.buyer;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.DomainException;
import nexusmarket.application.domain.models.Address;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.ports.out.BuyerRepositoryPort;

@Service
@RequiredArgsConstructor
public class BuyerService {

    private final BuyerRepositoryPort buyerRepositoryPort;

    public Buyer registerBuyer(Buyer buyer) {
        if (buyer == null) {
            throw new IllegalArgumentException("Buyer is required");
        }
        if (buyerRepositoryPort.existsByEmail(buyer.getEmail())) {
            throw new DomainException("Buyer email already exists");
        }
        return buyerRepositoryPort.save(buyer);
    }

    public Buyer updateBuyer(Buyer buyer) {
        if (buyer == null) {
            throw new IllegalArgumentException("Buyer is required");
        }
        return buyerRepositoryPort.save(buyer);
    }

    public Buyer findById(String buyerId) {
        return buyerRepositoryPort.findById(buyerId)
                .orElseThrow(() -> new DomainException("Buyer was not found"));
    }

    public void addPrimaryAddress(String buyerId, Address address) {
        Buyer buyer = findById(buyerId);
        buyer.setPrimaryAddress(address);
        buyerRepositoryPort.save(buyer);
    }

    public void addAdditionalAddress(String buyerId, Address address) {
        Buyer buyer = findById(buyerId);
        buyer.addAdditionalAddress(address);
        buyerRepositoryPort.save(buyer);
    }

    public Optional<Buyer> findByEmail(String email) {
        return buyerRepositoryPort.findByEmail(email);
    }
}