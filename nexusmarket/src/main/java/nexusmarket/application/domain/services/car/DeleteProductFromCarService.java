package nexusmarket.application.domain.services.car;

import org.springframework.stereotype.Service;

import nexusmarket.application.domain.models.Car;

@Service
public class DeleteProductFromCarService {

    public Car deleteItem(Car car, String productId) {
        if (car == null) {
            throw new IllegalArgumentException("Cart is required");
        }
        car.removeItem(productId);
        return car;
    }
}