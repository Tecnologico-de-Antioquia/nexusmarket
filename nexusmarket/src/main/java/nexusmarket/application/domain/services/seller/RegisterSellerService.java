package nexusmarket.application.domain.services.seller;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.InvalidRequestException;
import nexusmarket.application.domain.exceptions.ResourceAlreadyExistsException;
import nexusmarket.application.domain.models.Seller;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.models.Warehouse;
import nexusmarket.application.domain.ports.out.PasswordServicePort;
import nexusmarket.application.domain.ports.out.SellerRepositoryPort;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import nexusmarket.application.domain.ports.out.WarehouseRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import nexusmarket.application.domain.valueobjects.UserStatus;
import nexusmarket.application.domain.valueobjects.WarehouseType;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterSellerService {

    private final SellerRepositoryPort sellerRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final WarehouseRepositoryPort warehouseRepositoryPort;
    private final PasswordServicePort passwordServicePort;
    private final AuthorizationService authorizationService;

    public Seller register(User admin, Seller seller, Warehouse firstWarehouse) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        if (seller == null) {
            throw new InvalidRequestException("El vendedor es obligatorio.");
        }
        if (seller.getFullName() == null || seller.getFullName().isBlank()) {
            throw new InvalidRequestException("El nombre completo es obligatorio.");
        }
        if (seller.getEmail() == null || seller.getEmail().isBlank()) {
            throw new InvalidRequestException("El correo electronico es obligatorio.");
        }
        if (seller.getUsername() == null || seller.getUsername().isBlank()) {
            throw new InvalidRequestException("El nombre de usuario es obligatorio.");
        }
        if (seller.getPassword() == null || seller.getPassword().isBlank()) {
            throw new InvalidRequestException("La contrasena es obligatoria.");
        }
        if (sellerRepositoryPort.existsByEmail(seller.getEmail()) || userRepositoryPort.existsByEmail(seller.getEmail())) {
            throw new ResourceAlreadyExistsException("El correo electronico ya existe.");
        }
        if (userRepositoryPort.existsByUsername(seller.getUsername())) {
            throw new ResourceAlreadyExistsException("El nombre de usuario ya existe.");
        }

        String id = UUID.randomUUID().toString();
        String encodedPassword = passwordServicePort.encrypt(seller.getPassword());
        seller.setId(id);
        seller.setPassword(encodedPassword);
        seller.setRole(SystemRole.SELLER);
        seller.setState(UserStatus.ACTIVE);
        Seller saved = sellerRepositoryPort.save(seller);

        User user = new User();
        user.setId(id);
        user.setFullName(seller.getFullName());
        user.setEmail(seller.getEmail());
        user.setUsername(seller.getUsername());
        user.setPassword(encodedPassword);
        user.setRole(SystemRole.SELLER);
        user.setState(UserStatus.ACTIVE);
        userRepositoryPort.save(user);

        if (firstWarehouse != null) {
            firstWarehouse.setId(UUID.randomUUID().toString());
            firstWarehouse.setOwner(saved);
            firstWarehouse.setType(WarehouseType.SELLER);
            warehouseRepositoryPort.save(firstWarehouse);
        }
        return saved;
    }
}
