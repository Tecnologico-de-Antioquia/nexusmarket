package nexusmarket.application.domain.services.buyer;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.InvalidRequestException;
import nexusmarket.application.domain.exceptions.ResourceAlreadyExistsException;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.BuyerRepositoryPort;
import nexusmarket.application.domain.ports.out.PasswordServicePort;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import nexusmarket.application.domain.valueobjects.CommercialState;
import nexusmarket.application.domain.valueobjects.SystemRole;
import nexusmarket.application.domain.valueobjects.UserStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterBuyerService {

    private final BuyerRepositoryPort buyerRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordServicePort passwordServicePort;

    public Buyer register(Buyer buyer) {
        if (buyer == null) {
            throw new InvalidRequestException("El comprador es obligatorio.");
        }
        if (buyer.getFullName() == null || buyer.getFullName().isBlank()) {
            throw new InvalidRequestException("El nombre completo es obligatorio.");
        }
        if (buyer.getEmail() == null || buyer.getEmail().isBlank()) {
            throw new InvalidRequestException("El correo electronico es obligatorio.");
        }
        if (buyer.getUsername() == null || buyer.getUsername().isBlank()) {
            throw new InvalidRequestException("El nombre de usuario es obligatorio.");
        }
        if (buyer.getPassword() == null || buyer.getPassword().isBlank()) {
            throw new InvalidRequestException("La contrasena es obligatoria.");
        }
        if (buyerRepositoryPort.existsByEmail(buyer.getEmail()) || userRepositoryPort.existsByEmail(buyer.getEmail())) {
            throw new ResourceAlreadyExistsException("El correo electronico ya existe.");
        }
        if (userRepositoryPort.existsByUsername(buyer.getUsername())) {
            throw new ResourceAlreadyExistsException("El nombre de usuario ya existe.");
        }

        String id = UUID.randomUUID().toString();
        String encodedPassword = passwordServicePort.encrypt(buyer.getPassword());
        buyer.setId(id);
        buyer.setPassword(encodedPassword);
        buyer.setRole(SystemRole.BUYER);
        buyer.setState(UserStatus.ACTIVE);
        buyer.setCommercialState(CommercialState.ACTIVE);
        Buyer saved = buyerRepositoryPort.save(buyer);

        User user = new User();
        user.setId(id);
        user.setFullName(buyer.getFullName());
        user.setEmail(buyer.getEmail());
        user.setUsername(buyer.getUsername());
        user.setPassword(encodedPassword);
        user.setRole(SystemRole.BUYER);
        user.setState(UserStatus.ACTIVE);
        userRepositoryPort.save(user);

        return saved;
    }
}
