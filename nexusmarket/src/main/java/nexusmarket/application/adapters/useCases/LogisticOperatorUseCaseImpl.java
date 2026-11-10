package nexusmarket.application.adapters.useCases;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.Shipment;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.models.Warehouse;
import nexusmarket.application.domain.ports.in.LogisticOperatorPort;
import nexusmarket.application.domain.services.inventory.ConsultInventoryService;
import nexusmarket.application.domain.services.inventory.InventoryService;
import nexusmarket.application.domain.services.order.ConsultOrderService;
import nexusmarket.application.domain.services.order.OrderTransitionService;
import nexusmarket.application.domain.services.shipment.ShipmentService;
import nexusmarket.application.domain.services.warehouse.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogisticOperatorUseCaseImpl implements LogisticOperatorPort {

    private final WarehouseService warehouseService;
    private final ConsultInventoryService consultInventoryService;
    private final InventoryService inventoryService;
    private final ConsultOrderService consultOrderService;
    private final OrderTransitionService orderTransitionService;
    private final ShipmentService shipmentService;

    @Override
    public List<Warehouse> consultWarehouses(User user) {
        return warehouseService.consultAll(user);
    }

    @Override
    public List<Inventory> consultInventory(User user) {
        return consultInventoryService.consultAll(user);
    }

    @Override
    public Inventory receiveStock(User user, String productId, String warehouseId, int quantity) {
        return inventoryService.receive(user, productId, warehouseId, quantity);
    }

    @Override
    public Inventory adjustInventory(User user, String productId, String warehouseId, int delta) {
        return inventoryService.adjust(user, productId, warehouseId, delta);
    }

    @Override
    public Inventory registerSaleOut(User user, String productId, String warehouseId, int quantity) {
        return inventoryService.saleOut(user, productId, warehouseId, quantity);
    }

    @Override
    public Inventory registerReturn(User user, String productId, String warehouseId, int quantity) {
        return inventoryService.returnStock(user, productId, warehouseId, quantity);
    }

    @Override
    public List<Order> consultOrders(User user) {
        return consultOrderService.consultAll(user);
    }

    @Override
    public Order dispatchOrder(User user, String orderId) {
        return orderTransitionService.dispatch(user, orderId);
    }

    @Override
    public Order deliverOrder(User user, String orderId) {
        return orderTransitionService.deliver(user, orderId);
    }

    @Override
    public Shipment createShipment(User user, Shipment shipment) {
        return shipmentService.create(user, shipment);
    }

    @Override
    public Shipment dispatchShipment(User user, String shipmentId) {
        return shipmentService.dispatch(user, shipmentId);
    }

    @Override
    public Shipment deliverShipment(User user, String shipmentId) {
        return shipmentService.deliver(user, shipmentId);
    }

    @Override
    public List<Shipment> consultShipments(User user) {
        return shipmentService.consultAll(user);
    }

    @Override
    public Shipment consultShipment(User user, String shipmentId) {
        return shipmentService.consultById(user, shipmentId);
    }
}
