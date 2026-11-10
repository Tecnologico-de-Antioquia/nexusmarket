package nexusmarket.application.domain.services.catalog;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.UnauthorizedOperationException;
import nexusmarket.application.domain.models.DigitalProduct;
import nexusmarket.application.domain.models.PhysicalProduct;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.ProductRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProductService {

    private final ProductRepositoryPort productRepositoryPort;
    private final AuthorizationService authorizationService;

    public Product update(User user, Product updates) {
        authorizationService.requireActiveRole(user, SystemRole.SELLER);
        Product stored = productRepositoryPort.findById(updates.getId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado."));
        requireOwnership(user, stored);
        if (updates.getName() != null && !updates.getName().isBlank()) {
            stored.setName(updates.getName());
        }
        if (updates.getDescription() != null) {
            stored.setDescription(updates.getDescription());
        }
        if (updates.getPrice() != null) {
            stored.setPrice(updates.getPrice());
        }
        if (stored instanceof PhysicalProduct physical && updates instanceof PhysicalProduct physicalUpdates) {
            if (physicalUpdates.getVariant() != null) {
                physical.setVariant(physicalUpdates.getVariant());
            }
        }
        if (stored instanceof DigitalProduct digital && updates instanceof DigitalProduct digitalUpdates) {
            if (digitalUpdates.getFormat() != null) {
                digital.setFormat(digitalUpdates.getFormat());
            }
            if (digitalUpdates.getDownloadUrl() != null) {
                digital.setDownloadUrl(digitalUpdates.getDownloadUrl());
            }
        }
        return productRepositoryPort.save(stored);
    }

    private void requireOwnership(User user, Product product) {
        if (product.getSeller() == null || !product.getSeller().getId().equals(user.getId())) {
            throw new UnauthorizedOperationException("El producto no pertenece al vendedor.");
        }
    }
}
