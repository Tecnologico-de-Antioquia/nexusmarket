package nexusmarket.application.domain.services.car;

import org.springframework.stereotype.Service;

import nexusmarket.application.domain.exceptions.DomainException;
import nexusmarket.application.domain.models.Car;
import nexusmarket.application.domain.models.CartItem;

@Service
public class UpdateProductFromCarService {

    public Car updateItem(Car car, String productId, Integer quantity) {
        if (car == null) {
            throw new IllegalArgumentException("Cart is required");
        }

        CartItem item = car.getItems().stream()
                .filter(current -> current.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new DomainException("Product is not in cart"));

        item.setQuantity(quantity);
        return car;
    }
}