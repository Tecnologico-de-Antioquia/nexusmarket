package nexusmarket.application.domain.services.car;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.exceptions.DomainException;
import nexusmarket.application.domain.models.Car;
import nexusmarket.application.domain.models.CartItem;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.Physical;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.ports.out.InventoryRepositoryPort;
import nexusmarket.application.domain.ports.out.ProductRepositoryPort;

@Service
@RequiredArgsConstructor
public class AddProductToCarService {

    private final ProductRepositoryPort productRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;

    public Car addProduct(Car car, String productId, Integer quantity) {
        if (car == null) {
            throw new IllegalArgumentException("Cart is required");
        }

        Product product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new DomainException("Product was not found in catalog"));

        if (product instanceof Physical physical) {
            List<Inventory> inventories = inventoryRepositoryPort.findByProductId(productId);
            int available = inventories.stream().mapToInt(Inventory::getAvailableQuantity).sum();
            if (available < quantity) {
                throw new DomainException("Insufficient available stock for product");
            }
        }

        CartItem item = new CartItem(productId, quantity);
        car.addItem(item);
        return car;
    }
}