package nexusmarket.application.domain.services.inventory;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.InventoryRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultInventoryService {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final AuthorizationService authorizationService;

    public List<Inventory> consultAll(User user) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SELLER,
                SystemRole.LOGISTIC_OPERATOR, SystemRole.SUPERVISOR);
        return inventoryRepositoryPort.findAll();
    }

    public List<Inventory> consultByProduct(User user, String productId) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SELLER,
                SystemRole.LOGISTIC_OPERATOR, SystemRole.SUPERVISOR);
        return inventoryRepositoryPort.findByProductId(productId);
    }

    public List<Inventory> consultByWarehouse(User user, String warehouseId) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SELLER,
                SystemRole.LOGISTIC_OPERATOR, SystemRole.SUPERVISOR);
        return inventoryRepositoryPort.findByWarehouseId(warehouseId);
    }

    public Inventory consultOne(User user, String productId, String warehouseId) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SELLER,
                SystemRole.LOGISTIC_OPERATOR, SystemRole.SUPERVISOR);
        return inventoryRepositoryPort.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado."));
    }
}
