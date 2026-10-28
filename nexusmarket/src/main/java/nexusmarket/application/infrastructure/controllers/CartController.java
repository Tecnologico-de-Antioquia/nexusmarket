package nexusmarket.application.infrastructure.controllers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.Car;
import nexusmarket.application.domain.services.car.AddProductToCarService;
import nexusmarket.application.domain.services.car.ClearCarService;
import nexusmarket.application.domain.services.car.DeleteProductFromCarService;
import nexusmarket.application.domain.services.car.UpdateProductFromCarService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CartController {

    private final AddProductToCarService addProductToCarService;
    private final UpdateProductFromCarService updateProductFromCarService;
    private final DeleteProductFromCarService deleteProductFromCarService;
    private final ClearCarService clearCarService;

    private final Map<String, Car> carts = new ConcurrentHashMap<>();

    @GetMapping("/cart/{buyerId}")
    public Car getCart(@PathVariable String buyerId) {
        return carts.getOrDefault(buyerId, createCart(buyerId));
    }

    @PostMapping("/cart/{buyerId}/items")
    public Car addItem(@PathVariable String buyerId, @RequestBody CartItemRequest request) {
        Car car = carts.computeIfAbsent(buyerId, id -> createCart(id));
        return addProductToCarService.addProduct(car, request.productId(), request.quantity());
    }

    @PutMapping("/cart/{buyerId}/items/{productId}")
    public Car updateItem(@PathVariable String buyerId,
                         @PathVariable String productId,
                         @RequestBody CartItemRequest request) {
        Car car = carts.computeIfAbsent(buyerId, id -> createCart(id));
        return updateProductFromCarService.updateItem(car, productId, request.quantity());
    }

    @DeleteMapping("/cart/{buyerId}/items/{productId}")
    public Car removeItem(@PathVariable String buyerId, @PathVariable String productId) {
        Car car = carts.computeIfAbsent(buyerId, id -> createCart(id));
        return deleteProductFromCarService.deleteItem(car, productId);
    }

    @DeleteMapping("/cart/{buyerId}")
    public Car clearCart(@PathVariable String buyerId) {
        Car car = carts.computeIfAbsent(buyerId, id -> createCart(id));
        return clearCarService.clear(car);
    }

    private Car createCart(String buyerId) {
        Car cart = new Car(UUID.randomUUID().toString(), buyerId);
        carts.put(buyerId, cart);
        return cart;
    }

    public record CartItemRequest(String productId, Integer quantity) {}
}
