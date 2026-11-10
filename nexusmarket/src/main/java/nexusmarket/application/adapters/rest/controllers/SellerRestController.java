package nexusmarket.application.adapters.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.rest.dtos.CatalogDtos;
import nexusmarket.application.adapters.rest.dtos.InventoryDtos;
import nexusmarket.application.adapters.rest.dtos.OrderDtos;
import nexusmarket.application.adapters.rest.dtos.SellerDtos;
import nexusmarket.application.adapters.rest.dtos.WarehouseDtos;
import nexusmarket.application.adapters.rest.mappers.RestMapper;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.PhysicalProduct;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.Seller;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.models.Warehouse;
import nexusmarket.application.domain.ports.in.SellerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerRestController {

    private final SellerPort sellerPort;

    @GetMapping("/profile")
    public ResponseEntity<SellerDtos.SellerResponse> consultMyProfile(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(RestMapper.toSellerResponse(sellerPort.consultMyProfile(user)));
    }

    @PutMapping("/profile")
    public ResponseEntity<SellerDtos.SellerResponse> updateMyProfile(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody SellerDtos.UpdateRequest request) {
        Seller seller = RestMapper.toDomain(request);
        seller.setId(user.getId());
        return ResponseEntity.ok(RestMapper.toSellerResponse(sellerPort.updateMyProfile(user, seller)));
    }

    @GetMapping("/products")
    public ResponseEntity<List<CatalogDtos.ProductResponse>> consultMyProducts(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(sellerPort.consultMyProducts(user).stream()
                .map(RestMapper::toProductResponse).toList());
    }

    @PostMapping("/products")
    public ResponseEntity<CatalogDtos.ProductResponse> registerProduct(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody CatalogDtos.ProductRequest request) {
        Product product = sellerPort.registerProduct(user, RestMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(RestMapper.toProductResponse(product));
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<CatalogDtos.ProductResponse> updateProduct(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String productId,
            @Valid @RequestBody CatalogDtos.ProductRequest request) {
        Product product = RestMapper.toDomain(request);
        product.setId(productId);
        return ResponseEntity.ok(RestMapper.toProductResponse(sellerPort.updateProduct(user, product)));
    }

    @PostMapping("/products/{productId}/publish")
    public ResponseEntity<CatalogDtos.ProductResponse> publishProduct(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String productId) {
        return ResponseEntity.ok(RestMapper.toProductResponse(sellerPort.publishProduct(user, productId)));
    }

    @PostMapping("/products/{productId}/suspend")
    public ResponseEntity<CatalogDtos.ProductResponse> suspendProduct(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String productId) {
        return ResponseEntity.ok(RestMapper.toProductResponse(sellerPort.suspendProduct(user, productId)));
    }

    @PostMapping("/products/{productId}/discontinue")
    public ResponseEntity<CatalogDtos.ProductResponse> discontinueProduct(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String productId) {
        return ResponseEntity.ok(RestMapper.toProductResponse(sellerPort.discontinueProduct(user, productId)));
    }

    @GetMapping("/warehouses")
    public ResponseEntity<List<WarehouseDtos.WarehouseResponse>> consultMyWarehouses(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(sellerPort.consultMyWarehouses(user).stream()
                .map(RestMapper::toWarehouseResponse).toList());
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<InventoryDtos.InventoryResponse>> consultMyInventory(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(sellerPort.consultMyInventory(user).stream()
                .map(RestMapper::toInventoryResponse).toList());
    }

    @PostMapping("/inventory")
    public ResponseEntity<InventoryDtos.InventoryResponse> registerInventory(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody InventoryDtos.InventoryRequest request) {
        Inventory inventory = new Inventory();
        PhysicalProduct product = new PhysicalProduct();
        product.setId(request.productId());
        inventory.setProduct(product);
        Warehouse warehouse = new Warehouse();
        warehouse.setId(request.warehouseId());
        inventory.setWarehouse(warehouse);
        inventory.setAvailableQuantity(request.availableQuantity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestMapper.toInventoryResponse(sellerPort.registerInventory(user, inventory)));
    }

    @PostMapping("/inventory/receive")
    public ResponseEntity<InventoryDtos.InventoryResponse> receiveStock(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody InventoryDtos.MovementRequest request) {
        return ResponseEntity.ok(RestMapper.toInventoryResponse(
                sellerPort.receiveStock(user, request.productId(), request.warehouseId(), request.quantity())));
    }

    @PostMapping("/inventory/reserve")
    public ResponseEntity<InventoryDtos.InventoryResponse> reserveStock(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody InventoryDtos.MovementRequest request) {
        return ResponseEntity.ok(RestMapper.toInventoryResponse(
                sellerPort.reserveStock(user, request.productId(), request.warehouseId(), request.quantity())));
    }

    @PostMapping("/inventory/release")
    public ResponseEntity<InventoryDtos.InventoryResponse> releaseReservation(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody InventoryDtos.MovementRequest request) {
        return ResponseEntity.ok(RestMapper.toInventoryResponse(
                sellerPort.releaseReservation(user, request.productId(), request.warehouseId(), request.quantity())));
    }

    @PostMapping("/inventory/sale-out")
    public ResponseEntity<InventoryDtos.InventoryResponse> saleOut(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody InventoryDtos.MovementRequest request) {
        return ResponseEntity.ok(RestMapper.toInventoryResponse(
                sellerPort.registerSaleOut(user, request.productId(), request.warehouseId(), request.quantity())));
    }

    @PostMapping("/inventory/adjust")
    public ResponseEntity<InventoryDtos.InventoryResponse> adjustInventory(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody InventoryDtos.MovementRequest request) {
        int delta = request.delta() != null ? request.delta() : request.quantity();
        return ResponseEntity.ok(RestMapper.toInventoryResponse(
                sellerPort.adjustInventory(user, request.productId(), request.warehouseId(), delta)));
    }

    @PostMapping("/inventory/return")
    public ResponseEntity<InventoryDtos.InventoryResponse> registerReturn(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody InventoryDtos.MovementRequest request) {
        return ResponseEntity.ok(RestMapper.toInventoryResponse(
                sellerPort.registerReturn(user, request.productId(), request.warehouseId(), request.quantity())));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDtos.OrderResponse>> consultMyOrders(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(sellerPort.consultMyOrders(user).stream()
                .map(RestMapper::toOrderResponse).toList());
    }
}
