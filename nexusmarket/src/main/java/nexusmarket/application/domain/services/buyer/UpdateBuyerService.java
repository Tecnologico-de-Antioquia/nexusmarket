package nexusmarket.application.domain.services.buyer;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.ResourceAlreadyExistsException;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.BuyerRepositoryPort;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateBuyerService {

    private final BuyerRepositoryPort buyerRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final AuthorizationService authorizationService;

    public Buyer updateMyProfile(User user, Buyer updates) {
        authorizationService.requireActiveRole(user, SystemRole.BUYER);
        Buyer stored = buyerRepositoryPort.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Comprador no encontrado."));
        if (updates.getFullName() != null && !updates.getFullName().isBlank()) {
            stored.setFullName(updates.getFullName());
        }
        if (updates.getEmail() != null && !updates.getEmail().isBlank()
                && !updates.getEmail().equalsIgnoreCase(stored.getEmail())) {
            if (buyerRepositoryPort.existsByEmail(updates.getEmail())) {
                throw new ResourceAlreadyExistsException("El correo electronico ya existe.");
            }
            stored.setEmail(updates.getEmail());
        }
        Buyer saved = buyerRepositoryPort.save(stored);
        userRepositoryPort.findById(user.getId()).ifPresent(account -> {
            account.setFullName(saved.getFullName());
            account.setEmail(saved.getEmail());
            userRepositoryPort.update(account);
        });
        return saved;
    }

    public Buyer activateCommercialState(User admin, String buyerId) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        Buyer stored = buyerRepositoryPort.findById(buyerId)
                .orElseThrow(() -> new EntityNotFoundException("Comprador no encontrado."));
        stored.activateCommercialState();
        return buyerRepositoryPort.save(stored);
    }

    public Buyer inactivateCommercialState(User admin, String buyerId) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        Buyer stored = buyerRepositoryPort.findById(buyerId)
                .orElseThrow(() -> new EntityNotFoundException("Comprador no encontrado."));
        stored.inactivateCommercialState();
        return buyerRepositoryPort.save(stored);
    }
}
