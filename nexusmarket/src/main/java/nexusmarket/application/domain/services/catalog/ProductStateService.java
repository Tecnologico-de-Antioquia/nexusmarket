package nexusmarket.application.domain.services.catalog;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.UnauthorizedOperationException;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.ProductRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductStateService {

    private final ProductRepositoryPort productRepositoryPort;
    private final AuthorizationService authorizationService;

    public Product publish(User user, String productId) {
        Product product = loadAndAuthorize(user, productId);
        product.publish();
        return productRepositoryPort.save(product);
    }

    public Product suspend(User user, String productId) {
        Product product = loadAndAuthorize(user, productId);
        product.suspend();
        return productRepositoryPort.save(product);
    }

    public Product discontinue(User user, String productId) {
        Product product = loadAndAuthorize(user, productId);
        product.discontinue();
        return productRepositoryPort.save(product);
    }

    private Product loadAndAuthorize(User user, String productId) {
        authorizationService.requireAnyRole(user, SystemRole.SELLER, SystemRole.ADMINISTRATOR);
        Product product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado."));
        if (SystemRole.SELLER.equals(user.getRole())) {
            if (product.getSeller() == null || !product.getSeller().getId().equals(user.getId())) {
                throw new UnauthorizedOperationException("El producto no pertenece al vendedor.");
            }
        }
        return product;
    }
}
