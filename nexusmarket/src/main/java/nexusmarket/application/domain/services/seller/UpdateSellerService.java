package nexusmarket.application.domain.services.seller;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.ResourceAlreadyExistsException;
import nexusmarket.application.domain.models.Seller;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.SellerRepositoryPort;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateSellerService {

    private final SellerRepositoryPort sellerRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final AuthorizationService authorizationService;

    public Seller updateMyProfile(User user, Seller updates) {
        authorizationService.requireActiveRole(user, SystemRole.SELLER);
        Seller stored = sellerRepositoryPort.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Vendedor no encontrado."));
        applyUpdates(stored, updates);
        Seller saved = sellerRepositoryPort.save(stored);
        userRepositoryPort.findById(user.getId()).ifPresent(account -> {
            account.setFullName(saved.getFullName());
            account.setEmail(saved.getEmail());
            userRepositoryPort.update(account);
        });
        return saved;
    }

    public Seller updateByAdmin(User admin, Seller updates) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        Seller stored = sellerRepositoryPort.findById(updates.getId())
                .orElseThrow(() -> new EntityNotFoundException("Vendedor no encontrado."));
        applyUpdates(stored, updates);
        return sellerRepositoryPort.save(stored);
    }

    private void applyUpdates(Seller stored, Seller updates) {
        if (updates.getFullName() != null && !updates.getFullName().isBlank()) {
            stored.setFullName(updates.getFullName());
        }
        if (updates.getEmail() != null && !updates.getEmail().isBlank()
                && !updates.getEmail().equalsIgnoreCase(stored.getEmail())) {
            if (sellerRepositoryPort.existsByEmail(updates.getEmail())) {
                throw new ResourceAlreadyExistsException("El correo electronico ya existe.");
            }
            stored.setEmail(updates.getEmail());
        }
        if (updates.getCompanyName() != null) {
            stored.setCompanyName(updates.getCompanyName());
        }
        if (updates.getPhoneNumber() != null) {
            stored.setPhoneNumber(updates.getPhoneNumber());
        }
    }
}
