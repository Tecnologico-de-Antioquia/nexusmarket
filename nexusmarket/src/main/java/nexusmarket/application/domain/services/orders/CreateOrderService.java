package nexusmarket.application.domain.services.orders;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.DomainException;
import nexusmarket.application.domain.models.Address;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.Shipment;
import nexusmarket.application.domain.models.Transaction;
import nexusmarket.application.domain.ports.out.InventoryRepositoryPort;
import nexusmarket.application.domain.ports.out.OrderRepositoryPort;
import nexusmarket.application.domain.ports.out.PaymentGatewayPort;
import nexusmarket.application.domain.ports.out.ShipmentProviderPort;

@Service
@RequiredArgsConstructor
public class CreateOrderService {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final PaymentGatewayPort paymentGatewayPort;
    private final ShipmentProviderPort shipmentProviderPort;

    public Order createOrder(String buyerId, List<String> productIds, BigDecimal totalAmount,
                            Address originAddress, Address destinationAddress) {
        if (buyerId == null || buyerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Buyer id is required");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product");
        }

        Order order = new Order(UUID.randomUUID().toString(), buyerId, productIds, totalAmount);

        if (!paymentGatewayPort.authorize(order.getId(), totalAmount)) {
            throw new DomainException("Payment authorization failed");
        }

        order.pay();
        Transaction transaction = new Transaction(UUID.randomUUID().toString(), order.getId(), buyerId, totalAmount);
        transaction.confirm();
        order.setTransaction(transaction);

        for (String productId : productIds) {
            List<Inventory> inventories = inventoryRepositoryPort.findByProductId(productId);
            if (inventories.isEmpty()) {
                throw new DomainException("No inventory found for product: " + productId);
            }
            for (Inventory inventory : inventories) {
                inventory.reserve(1);
                inventoryRepositoryPort.save(inventory);
            }
        }

        String shipmentId = shipmentProviderPort.createShipment(order.getId(), originAddress, destinationAddress);
        Shipment shipment = new Shipment(shipmentId, order.getId(), originAddress, destinationAddress);
        shipment.dispatch();
        order.setShipment(shipment);

        return orderRepositoryPort.save(order);
    }
}