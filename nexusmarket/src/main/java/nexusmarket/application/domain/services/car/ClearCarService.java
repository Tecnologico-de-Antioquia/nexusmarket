package nexusmarket.application.domain.services.car;

import org.springframework.stereotype.Service;

import nexusmarket.application.domain.models.Car;

@Service
public class ClearCarService {

    public Car clear(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Cart is required");
        }
        car.clear();
        return car;
    }
}