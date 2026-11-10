package nexusmarket.application.domain.services.seller;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.models.Seller;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.SellerRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultSellerService {

    private final SellerRepositoryPort sellerRepositoryPort;
    private final AuthorizationService authorizationService;

    public Seller consultMyProfile(User user) {
        authorizationService.requireActiveRole(user, SystemRole.SELLER);
        return sellerRepositoryPort.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Vendedor no encontrado."));
    }

    public Seller consultById(User admin, String sellerId) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        return sellerRepositoryPort.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Vendedor no encontrado."));
    }

    public List<Seller> consultAll(User admin) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        return sellerRepositoryPort.findAll();
    }
}
