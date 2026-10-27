package nexusmarket.application.domain.services.orders;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.DomainException;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.ports.out.InventoryRepositoryPort;
import nexusmarket.application.domain.ports.out.OrderRepositoryPort;
import nexusmarket.application.domain.valueObjects.OrderState;

@Service
@RequiredArgsConstructor
public class CancelOrderService {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;

    public Order cancel(String orderId) {
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new DomainException("Order was not found"));

        if (order.getState() == OrderState.COMPLETED) {
            throw new DomainException("A completed order cannot be cancelled");
        }

        order.cancel();
        for (String productId : order.getProductIds()) {
            List<Inventory> inventories = inventoryRepositoryPort.findByProductId(productId);
            for (Inventory inventory : inventories) {
                if (inventory.getReservedQuantity() > 0) {
                    inventory.releaseReservation(1);
                    inventoryRepositoryPort.save(inventory);
                }
            }
        }

        return orderRepositoryPort.save(order);
    }
}