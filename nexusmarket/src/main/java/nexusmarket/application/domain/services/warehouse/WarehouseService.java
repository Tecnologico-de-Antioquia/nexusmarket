package nexusmarket.application.domain.services.warehouse;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.InvalidRequestException;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.models.Warehouse;
import nexusmarket.application.domain.ports.out.WarehouseRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.SystemRole;
import nexusmarket.application.domain.valueobjects.WarehouseType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepositoryPort warehouseRepositoryPort;
    private final AuthorizationService authorizationService;

    public Warehouse register(User admin, Warehouse warehouse) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        if (warehouse == null || warehouse.getName() == null || warehouse.getName().isBlank()) {
            throw new InvalidRequestException("El nombre de la bodega es obligatorio.");
        }
        warehouse.setId(UUID.randomUUID().toString());
        warehouse.setType(warehouse.getOwner() == null ? WarehouseType.MARKETPLACE : WarehouseType.SELLER);
        return warehouseRepositoryPort.save(warehouse);
    }

    public Warehouse update(User admin, Warehouse warehouse) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        Warehouse stored = warehouseRepositoryPort.findById(warehouse.getId())
                .orElseThrow(() -> new EntityNotFoundException("Bodega no encontrada."));
        if (warehouse.getName() != null) {
            stored.setName(warehouse.getName());
        }
        if (warehouse.getAddress() != null) {
            stored.setAddress(warehouse.getAddress());
        }
        if (warehouse.getCapacity() != null) {
            stored.setCapacity(warehouse.getCapacity());
        }
        if (warehouse.getOwner() != null) {
            stored.setOwner(warehouse.getOwner());
            stored.setType(WarehouseType.SELLER);
        }
        return warehouseRepositoryPort.save(stored);
    }

    public void delete(User admin, String warehouseId) {
        authorizationService.requireActiveRole(admin, SystemRole.ADMINISTRATOR);
        warehouseRepositoryPort.findById(warehouseId)
                .orElseThrow(() -> new EntityNotFoundException("Bodega no encontrada."));
        warehouseRepositoryPort.delete(warehouseId);
    }

    public List<Warehouse> consultAll(User user) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SELLER,
                SystemRole.LOGISTIC_OPERATOR, SystemRole.SUPERVISOR);
        return warehouseRepositoryPort.findAll();
    }

    public List<Warehouse> consultBySeller(User user) {
        authorizationService.requireActiveRole(user, SystemRole.SELLER);
        return warehouseRepositoryPort.findBySellerId(user.getId());
    }

    public Warehouse consultById(User user, String warehouseId) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SELLER,
                SystemRole.LOGISTIC_OPERATOR, SystemRole.SUPERVISOR);
        return warehouseRepositoryPort.findById(warehouseId)
                .orElseThrow(() -> new EntityNotFoundException("Bodega no encontrada."));
    }
}
