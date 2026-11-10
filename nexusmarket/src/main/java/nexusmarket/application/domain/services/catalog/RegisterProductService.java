package nexusmarket.application.domain.services.catalog;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.InvalidRequestException;
import nexusmarket.application.domain.models.PhysicalProduct;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.Seller;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.ProductRepositoryPort;
import nexusmarket.application.domain.ports.out.SellerRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.ProductState;
import nexusmarket.application.domain.valueobjects.ProductType;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterProductService {

    private final ProductRepositoryPort productRepositoryPort;
    private final SellerRepositoryPort sellerRepositoryPort;
    private final AuthorizationService authorizationService;

    public Product register(User user, Product product) {
        authorizationService.requireActiveRole(user, SystemRole.SELLER);
        if (product == null || product.getName() == null || product.getName().isBlank()) {
            throw new InvalidRequestException("El nombre del producto es obligatorio.");
        }
        Seller seller = sellerRepositoryPort.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Vendedor no encontrado."));
        product.setId(UUID.randomUUID().toString());
        product.setSeller(seller);
        if (product.getProductType() == null) {
            product.setProductType(product instanceof PhysicalProduct ? ProductType.PHYSICAL : ProductType.DIGITAL);
        }
        product.setState(ProductState.PUBLISHED);
        return productRepositoryPort.save(product);
    }
}
