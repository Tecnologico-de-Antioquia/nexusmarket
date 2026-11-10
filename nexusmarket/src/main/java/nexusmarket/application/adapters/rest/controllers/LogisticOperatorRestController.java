package nexusmarket.application.adapters.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.rest.dtos.InventoryDtos;
import nexusmarket.application.adapters.rest.dtos.OrderDtos;
import nexusmarket.application.adapters.rest.dtos.ShipmentDtos;
import nexusmarket.application.adapters.rest.dtos.WarehouseDtos;
import nexusmarket.application.adapters.rest.mappers.RestMapper;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.Shipment;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.in.LogisticOperatorPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logistic-operator")
@RequiredArgsConstructor
public class LogisticOperatorRestController {

    private final LogisticOperatorPort logisticOperatorPort;

    @GetMapping("/warehouses")
    public ResponseEntity<List<WarehouseDtos.WarehouseResponse>> consultWarehouses(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(logisticOperatorPort.consultWarehouses(user).stream()
                .map(RestMapper::toWarehouseResponse).toList());
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<InventoryDtos.InventoryResponse>> consultInventory(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(logisticOperatorPort.consultInventory(user).stream()
                .map(RestMapper::toInventoryResponse).toList());
    }

    @PostMapping("/inventory/receive")
    public ResponseEntity<InventoryDtos.InventoryResponse> receiveStock(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody InventoryDtos.MovementRequest request) {
        return ResponseEntity.ok(RestMapper.toInventoryResponse(
                logisticOperatorPort.receiveStock(user, request.productId(), request.warehouseId(),
                        request.quantity())));
    }

    @PostMapping("/inventory/adjust")
    public ResponseEntity<InventoryDtos.InventoryResponse> adjustInventory(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody InventoryDtos.MovementRequest request) {
        int delta = request.delta() != null ? request.delta() : request.quantity();
        return ResponseEntity.ok(RestMapper.toInventoryResponse(
                logisticOperatorPort.adjustInventory(user, request.productId(), request.warehouseId(), delta)));
    }

    @PostMapping("/inventory/sale-out")
    public ResponseEntity<InventoryDtos.InventoryResponse> saleOut(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody InventoryDtos.MovementRequest request) {
        return ResponseEntity.ok(RestMapper.toInventoryResponse(
                logisticOperatorPort.registerSaleOut(user, request.productId(), request.warehouseId(),
                        request.quantity())));
    }

    @PostMapping("/inventory/return")
    public ResponseEntity<InventoryDtos.InventoryResponse> registerReturn(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody InventoryDtos.MovementRequest request) {
        return ResponseEntity.ok(RestMapper.toInventoryResponse(
                logisticOperatorPort.registerReturn(user, request.productId(), request.warehouseId(),
                        request.quantity())));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDtos.OrderResponse>> consultOrders(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(logisticOperatorPort.consultOrders(user).stream()
                .map(RestMapper::toOrderResponse).toList());
    }

    @PostMapping("/orders/{orderId}/dispatch")
    public ResponseEntity<OrderDtos.OrderResponse> dispatchOrder(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String orderId) {
        return ResponseEntity.ok(RestMapper.toOrderResponse(logisticOperatorPort.dispatchOrder(user, orderId)));
    }

    @PostMapping("/orders/{orderId}/deliver")
    public ResponseEntity<OrderDtos.OrderResponse> deliverOrder(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String orderId) {
        return ResponseEntity.ok(RestMapper.toOrderResponse(logisticOperatorPort.deliverOrder(user, orderId)));
    }

    @GetMapping("/shipments")
    public ResponseEntity<List<ShipmentDtos.ShipmentResponse>> consultShipments(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(logisticOperatorPort.consultShipments(user).stream()
                .map(RestMapper::toShipmentResponse).toList());
    }

    @GetMapping("/shipments/{shipmentId}")
    public ResponseEntity<ShipmentDtos.ShipmentResponse> consultShipment(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String shipmentId) {
        return ResponseEntity.ok(RestMapper.toShipmentResponse(
                logisticOperatorPort.consultShipment(user, shipmentId)));
    }

    @PostMapping("/shipments")
    public ResponseEntity<ShipmentDtos.ShipmentResponse> createShipment(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody ShipmentDtos.ShipmentRequest request) {
        Shipment shipment = new Shipment();
        Order order = new Order();
        order.setId(request.orderId());
        shipment.setOrder(order);
        shipment.setOriginAddress(RestMapper.toDomain(request.originAddress()));
        shipment.setDestinationAddress(RestMapper.toDomain(request.destinationAddress()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestMapper.toShipmentResponse(logisticOperatorPort.createShipment(user, shipment)));
    }

    @PostMapping("/shipments/{shipmentId}/dispatch")
    public ResponseEntity<ShipmentDtos.ShipmentResponse> dispatchShipment(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String shipmentId) {
        return ResponseEntity.ok(RestMapper.toShipmentResponse(
                logisticOperatorPort.dispatchShipment(user, shipmentId)));
    }

    @PostMapping("/shipments/{shipmentId}/deliver")
    public ResponseEntity<ShipmentDtos.ShipmentResponse> deliverShipment(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String shipmentId) {
        return ResponseEntity.ok(RestMapper.toShipmentResponse(
                logisticOperatorPort.deliverShipment(user, shipmentId)));
    }
}
