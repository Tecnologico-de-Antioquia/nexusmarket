package nexusmarket.application.infrastructure.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.Address;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.services.orders.CancelOrderService;
import nexusmarket.application.domain.services.orders.CreateOrderService;
import nexusmarket.application.domain.services.orders.GetOrderService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderService createOrderService;
    private final GetOrderService getOrderService;
    private final CancelOrderService cancelOrderService;

    @PostMapping("/orders")
    public Order createOrder(@RequestBody OrderRequest request) {
        Address originAddress = new Address(
                request.originStreet(),
                request.originCity(),
                request.originState(),
                request.originPostalCode(),
                request.originCountry());

        Address destinationAddress = new Address(
                request.destinationStreet(),
                request.destinationCity(),
                request.destinationState(),
                request.destinationPostalCode(),
                request.destinationCountry());

        return createOrderService.createOrder(
                request.buyerId(),
                request.productIds(),
                request.totalAmount(),
                originAddress,
                destinationAddress);
    }

    @GetMapping("/orders/{id}")
    public Order getOrder(@PathVariable String id) {
        return getOrderService.findById(id);
    }

    @GetMapping("/orders/buyer/{buyerId}")
    public List<Order> getOrdersByBuyer(@PathVariable String buyerId) {
        return getOrderService.findByBuyerId(buyerId);
    }

    @DeleteMapping("/orders/{id}")
    public Order cancelOrder(@PathVariable String id) {
        return cancelOrderService.cancel(id);
    }

    public record OrderRequest(
            String buyerId,
            List<String> productIds,
            BigDecimal totalAmount,
            String originStreet,
            String originCity,
            String originState,
            String originPostalCode,
            String originCountry,
            String destinationStreet,
            String destinationCity,
            String destinationState,
            String destinationPostalCode,
            String destinationCountry) {}
}
