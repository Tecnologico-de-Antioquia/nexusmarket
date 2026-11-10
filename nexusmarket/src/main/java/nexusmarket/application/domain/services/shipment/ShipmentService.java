package nexusmarket.application.domain.services.shipment;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.EntityNotFoundException;
import nexusmarket.application.domain.exceptions.InvalidRequestException;
import nexusmarket.application.domain.models.Address;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.OrderItem;
import nexusmarket.application.domain.models.Shipment;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.InventoryRepositoryPort;
import nexusmarket.application.domain.ports.out.ShipmentProviderPort;
import nexusmarket.application.domain.ports.out.ShipmentRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.ShipmentState;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepositoryPort shipmentRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final ShipmentProviderPort shipmentProviderPort;
    private final AuthorizationService authorizationService;

    public Shipment createForOrder(User actor, Order order) {
        Address origin = resolveOrigin(order);
        Address destination = order.getBuyer() == null ? null : order.getBuyer().getPrimaryAddress();
        String guide = shipmentProviderPort.createShipment(order.getId(), origin, destination);
        Shipment shipment = new Shipment();
        shipment.setId(UUID.randomUUID().toString());
        shipment.setOrder(order);
        shipment.setOriginAddress(origin);
        shipment.setDestinationAddress(destination);
        shipment.setTrackingGuide(guide);
        shipment.setState(ShipmentState.PENDING);
        return shipmentRepositoryPort.save(shipment);
    }

    public Shipment create(User user, Shipment shipment) {
        authorizationService.requireActiveRole(user, SystemRole.LOGISTIC_OPERATOR);
        if (shipment == null || shipment.getOrder() == null || shipment.getOrder().getId() == null) {
            throw new InvalidRequestException("El envio debe estar vinculado a un pedido.");
        }
        shipment.setId(UUID.randomUUID().toString());
        if (shipment.getTrackingGuide() == null) {
            shipment.setTrackingGuide(shipmentProviderPort.createShipment(
                    shipment.getOrder().getId(), shipment.getOriginAddress(), shipment.getDestinationAddress()));
        }
        shipment.setState(ShipmentState.PENDING);
        return shipmentRepositoryPort.save(shipment);
    }

    public Shipment dispatch(User user, String shipmentId) {
        authorizationService.requireActiveRole(user, SystemRole.LOGISTIC_OPERATOR);
        Shipment shipment = load(shipmentId);
        shipment.dispatch();
        shipmentProviderPort.updateTrackingStatus(shipment.getId(), ShipmentState.DISPATCHED.getCode());
        return shipmentRepositoryPort.save(shipment);
    }

    public Shipment deliver(User user, String shipmentId) {
        authorizationService.requireActiveRole(user, SystemRole.LOGISTIC_OPERATOR);
        Shipment shipment = load(shipmentId);
        shipment.deliver();
        shipmentProviderPort.updateTrackingStatus(shipment.getId(), ShipmentState.DELIVERED.getCode());
        return shipmentRepositoryPort.save(shipment);
    }

    public Shipment consultById(User user, String shipmentId) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.LOGISTIC_OPERATOR,
                SystemRole.SUPERVISOR);
        return load(shipmentId);
    }

    public List<Shipment> consultAll(User user) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.LOGISTIC_OPERATOR,
                SystemRole.SUPERVISOR);
        return shipmentRepositoryPort.findAll();
    }

    private Shipment load(String shipmentId) {
        return shipmentRepositoryPort.findById(shipmentId)
                .orElseThrow(() -> new EntityNotFoundException("Envio no encontrado."));
    }

    private Address resolveOrigin(Order order) {
        for (OrderItem item : order.getItems()) {
            List<Inventory> inventories = inventoryRepositoryPort.findByProductId(item.getProduct().getId());
            if (!inventories.isEmpty() && inventories.get(0).getWarehouse() != null) {
                return inventories.get(0).getWarehouse().getAddress();
            }
        }
        return order.getBuyer() == null ? null : order.getBuyer().getPrimaryAddress();
    }
}
