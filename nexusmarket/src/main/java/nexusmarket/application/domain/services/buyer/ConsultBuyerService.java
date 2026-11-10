package nexusmarket.application.domain.services.buyer;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.BuyerRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultBuyerService {

    private final BuyerRepositoryPort buyerRepositoryPort;
    private final AuthorizationService authorizationService;

    public Buyer consultMyProfile(User user) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        return buyerRepositoryPort.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Comprador no encontrado."));
    }

    public Buyer consultById(User admin, String buyerId) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        return buyerRepositoryPort.findById(buyerId)
                .orElseThrow(() -> new EntityNotFoundException("Comprador no encontrado."));
    }

    public List<Buyer> consultAll(User admin) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        return buyerRepositoryPort.findAll();
    }
}
