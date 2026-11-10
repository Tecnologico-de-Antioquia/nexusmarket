package nexusmarket.application.domain.ports.in;

import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.Shipment;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.models.Warehouse;

import java.util.List;

public interface LogisticOperatorPort {

    List<Warehouse> consultWarehouses(User user);

    List<Inventory> consultInventory(User user);

    Inventory receiveStock(User user, String productId, String warehouseId, int quantity);

    Inventory adjustInventory(User user, String productId, String warehouseId, int delta);

    Inventory registerSaleOut(User user, String productId, String warehouseId, int quantity);

    Inventory registerReturn(User user, String productId, String warehouseId, int quantity);

    List<Order> consultOrders(User user);

    Order dispatchOrder(User user, String orderId);

    Order deliverOrder(User user, String orderId);

    Shipment createShipment(User user, Shipment shipment);

    Shipment dispatchShipment(User user, String shipmentId);

    Shipment deliverShipment(User user, String shipmentId);

    List<Shipment> consultShipments(User user);

    Shipment consultShipment(User user, String shipmentId);
}
