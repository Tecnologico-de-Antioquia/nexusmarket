package nexusmarket.application.domain.services.catalog;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.ProductRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultProductService {

    private final ProductRepositoryPort productRepositoryPort;
    private final AuthorizationService authorizationService;

    public List<Product> consultCatalog(User user) {
        authorizationService.requireAuthenticated(user);
        return productRepositoryPort.findPublished();
    }

    public Product consultById(User user, String productId) {
        authorizationService.requireAuthenticated(user);
        return productRepositoryPort.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado."));
    }

    public List<Product> consultBySeller(User user) {
        authorizationService.requireActiveRole(user, SystemRole.SELLER);
        return productRepositoryPort.findBySellerId(user.getId());
    }

    public List<Product> consultAll(User admin) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        return productRepositoryPort.findAll();
    }
}
