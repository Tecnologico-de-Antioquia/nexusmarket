package nexusmarket.application.domain.services.buyer;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.InvalidRequestException;
import nexusmarket.application.domain.models.Address;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.BuyerRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final BuyerRepositoryPort buyerRepositoryPort;
    private final AuthorizationService authorizationService;

    public Buyer addAddress(User user, Address address) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        if (address == null) {
            throw new InvalidRequestException("La direccion es obligatoria.");
        }
        Buyer buyer = buyerRepositoryPort.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Comprador no encontrado."));
        address.setId(UUID.randomUUID().toString());
        if (buyer.getPrimaryAddress() == null) {
            buyer.setPrimaryAddress(address);
        } else {
            buyer.addAdditionalAddress(address);
        }
        return buyerRepositoryPort.save(buyer);
    }

    public List<Address> consultAddresses(User user) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        Buyer buyer = buyerRepositoryPort.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Comprador no encontrado."));
        List<Address> addresses = new ArrayList<>();
        if (buyer.getPrimaryAddress() != null) {
            addresses.add(buyer.getPrimaryAddress());
        }
        if (buyer.getAdditionalAddresses() != null) {
            addresses.addAll(buyer.getAdditionalAddresses());
        }
        return addresses;
    }
}
