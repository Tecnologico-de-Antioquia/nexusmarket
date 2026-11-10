package nexusmarket.application.adapters.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.rest.dtos.AdminDtos;
import nexusmarket.application.adapters.rest.dtos.CatalogDtos;
import nexusmarket.application.adapters.rest.dtos.InventoryDtos;
import nexusmarket.application.adapters.rest.dtos.OrderDtos;
import nexusmarket.application.adapters.rest.dtos.ReturnDtos;
import nexusmarket.application.adapters.rest.dtos.SellerDtos;
import nexusmarket.application.adapters.rest.dtos.WarehouseDtos;
import nexusmarket.application.adapters.rest.mappers.RestMapper;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.models.Warehouse;
import nexusmarket.application.domain.ports.in.AdministratorPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdministratorRestController {

    private final AdministratorPort administratorPort;

    @GetMapping("/users")
    public ResponseEntity<List<AdminDtos.UserResponse>> consultUsers(
            @AuthenticationPrincipal(expression = "user") User admin) {
        return ResponseEntity.ok(administratorPort.consultUsers(admin).stream()
                .map(RestMapper::toUserResponse).toList());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<AdminDtos.UserResponse> consultUser(
            @AuthenticationPrincipal(expression = "user") User admin,
            @PathVariable String userId) {
        return ResponseEntity.ok(RestMapper.toUserResponse(administratorPort.consultUser(admin, userId)));
    }

    @PostMapping("/users")
    public ResponseEntity<AdminDtos.UserResponse> registerUser(
            @AuthenticationPrincipal(expression = "user") User admin,
            @Valid @RequestBody AdminDtos.UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestMapper.toUserResponse(
                        administratorPort.registerUser(admin, RestMapper.toDomain(request))));
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<AdminDtos.UserResponse> updateUser(
            @AuthenticationPrincipal(expression = "user") User admin,
            @PathVariable String userId,
            @Valid @RequestBody AdminDtos.UpdateUserRequest request) {
        User user = RestMapper.toDomain(request);
        user.setId(userId);
        return ResponseEntity.ok(RestMapper.toUserResponse(administratorPort.updateUser(admin, user)));
    }

    @PostMapping("/users/{userId}/block")
    public ResponseEntity<AdminDtos.UserResponse> blockUser(
            @AuthenticationPrincipal(expression = "user") User admin,
            @PathVariable String userId) {
        return ResponseEntity.ok(RestMapper.toUserResponse(administratorPort.blockUser(admin, userId)));
    }

    @PostMapping("/users/{userId}/activate")
    public ResponseEntity<AdminDtos.UserResponse> activateUser(
            @AuthenticationPrincipal(expression = "user") User admin,
            @PathVariable String userId) {
        return ResponseEntity.ok(RestMapper.toUserResponse(administratorPort.activateUser(admin, userId)));
    }

    @GetMapping("/sellers")
    public ResponseEntity<List<SellerDtos.SellerResponse>> consultSellers(
            @AuthenticationPrincipal(expression = "user") User admin) {
        return ResponseEntity.ok(administratorPort.consultSellers(admin).stream()
                .map(RestMapper::toSellerResponse).toList());
    }

    @GetMapping("/sellers/{sellerId}")
    public ResponseEntity<SellerDtos.SellerResponse> consultSeller(
            @AuthenticationPrincipal(expression = "user") User admin,
            @PathVariable String sellerId) {
        return ResponseEntity.ok(RestMapper.toSellerResponse(administratorPort.consultSeller(admin, sellerId)));
    }

    @PostMapping("/sellers")
    public ResponseEntity<SellerDtos.SellerResponse> registerSeller(
            @AuthenticationPrincipal(expression = "user") User admin,
            @Valid @RequestBody SellerDtos.RegisterRequest request) {
        Warehouse firstWarehouse = RestMapper.toDomain(request.firstWarehouse());
        return ResponseEntity.status(HttpStatus.CREATED).body(RestMapper.toSellerResponse(
                administratorPort.registerSeller(admin, RestMapper.toDomain(request), firstWarehouse)));
    }

    @PutMapping("/sellers/{sellerId}")
    public ResponseEntity<SellerDtos.SellerResponse> updateSeller(
            @AuthenticationPrincipal(expression = "user") User admin,
            @PathVariable String sellerId,
            @Valid @RequestBody SellerDtos.UpdateRequest request) {
        var seller = RestMapper.toDomain(request);
        seller.setId(sellerId);
        return ResponseEntity.ok(RestMapper.toSellerResponse(administratorPort.updateSeller(admin, seller)));
    }

    @GetMapping("/warehouses")
    public ResponseEntity<List<WarehouseDtos.WarehouseResponse>> consultWarehouses(
            @AuthenticationPrincipal(expression = "user") User admin) {
        return ResponseEntity.ok(administratorPort.consultWarehouses(admin).stream()
                .map(RestMapper::toWarehouseResponse).toList());
    }

    @PostMapping("/warehouses")
    public ResponseEntity<WarehouseDtos.WarehouseResponse> registerWarehouse(
            @AuthenticationPrincipal(expression = "user") User admin,
            @Valid @RequestBody WarehouseDtos.WarehouseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(RestMapper.toWarehouseResponse(
                administratorPort.registerWarehouse(admin, RestMapper.toDomain(request))));
    }

    @PutMapping("/warehouses/{warehouseId}")
    public ResponseEntity<WarehouseDtos.WarehouseResponse> updateWarehouse(
            @AuthenticationPrincipal(expression = "user") User admin,
            @PathVariable String warehouseId,
            @Valid @RequestBody WarehouseDtos.WarehouseRequest request) {
        Warehouse warehouse = RestMapper.toDomain(request);
        warehouse.setId(warehouseId);
        return ResponseEntity.ok(RestMapper.toWarehouseResponse(
                administratorPort.updateWarehouse(admin, warehouse)));
    }

    @DeleteMapping("/warehouses/{warehouseId}")
    public ResponseEntity<Void> deleteWarehouse(
            @AuthenticationPrincipal(expression = "user") User admin,
            @PathVariable String warehouseId) {
        administratorPort.deleteWarehouse(admin, warehouseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products")
    public ResponseEntity<List<CatalogDtos.ProductResponse>> consultProducts(
            @AuthenticationPrincipal(expression = "user") User admin) {
        return ResponseEntity.ok(administratorPort.consultProducts(admin).stream()
                .map(RestMapper::toProductResponse).toList());
    }

    @PostMapping("/products/{productId}/suspend")
    public ResponseEntity<CatalogDtos.ProductResponse> suspendProduct(
            @AuthenticationPrincipal(expression = "user") User admin,
            @PathVariable String productId) {
        return ResponseEntity.ok(RestMapper.toProductResponse(
                administratorPort.suspendProduct(admin, productId)));
    }

    @PostMapping("/products/{productId}/discontinue")
    public ResponseEntity<CatalogDtos.ProductResponse> discontinueProduct(
            @AuthenticationPrincipal(expression = "user") User admin,
            @PathVariable String productId) {
        return ResponseEntity.ok(RestMapper.toProductResponse(
                administratorPort.discontinueProduct(admin, productId)));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDtos.OrderResponse>> consultOrders(
            @AuthenticationPrincipal(expression = "user") User admin) {
        return ResponseEntity.ok(administratorPort.consultOrders(admin).stream()
                .map(RestMapper::toOrderResponse).toList());
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDtos.OrderResponse> consultOrder(
            @AuthenticationPrincipal(expression = "user") User admin,
            @PathVariable String orderId) {
        return ResponseEntity.ok(RestMapper.toOrderResponse(administratorPort.consultOrder(admin, orderId)));
    }

    @GetMapping("/returns")
    public ResponseEntity<List<ReturnDtos.ReturnResponse>> consultReturns(
            @AuthenticationPrincipal(expression = "user") User admin) {
        return ResponseEntity.ok(administratorPort.consultReturns(admin).stream()
                .map(RestMapper::toReturnResponse).toList());
    }

    @PostMapping("/returns/{returnId}/approve")
    public ResponseEntity<ReturnDtos.ReturnResponse> approveReturn(
            @AuthenticationPrincipal(expression = "user") User admin,
            @PathVariable String returnId) {
        return ResponseEntity.ok(RestMapper.toReturnResponse(administratorPort.approveReturn(admin, returnId)));
    }

    @PostMapping("/returns/{returnId}/reject")
    public ResponseEntity<ReturnDtos.ReturnResponse> rejectReturn(
            @AuthenticationPrincipal(expression = "user") User admin,
            @PathVariable String returnId) {
        return ResponseEntity.ok(RestMapper.toReturnResponse(administratorPort.rejectReturn(admin, returnId)));
    }

    @PostMapping("/returns/{returnId}/receive")
    public ResponseEntity<ReturnDtos.ReturnResponse> receiveReturn(
            @AuthenticationPrincipal(expression = "user") User admin,
            @PathVariable String returnId) {
        return ResponseEntity.ok(RestMapper.toReturnResponse(administratorPort.receiveReturn(admin, returnId)));
    }

    @GetMapping("/refunds")
    public ResponseEntity<List<ReturnDtos.RefundResponse>> consultRefunds(
            @AuthenticationPrincipal(expression = "user") User admin) {
        return ResponseEntity.ok(administratorPort.consultRefunds(admin).stream()
                .map(RestMapper::toRefundResponse).toList());
    }

    @PostMapping("/refunds/{refundId}/process")
    public ResponseEntity<ReturnDtos.RefundResponse> processRefund(
            @AuthenticationPrincipal(expression = "user") User admin,
            @PathVariable String refundId) {
        return ResponseEntity.ok(RestMapper.toRefundResponse(administratorPort.processRefund(admin, refundId)));
    }

    @GetMapping("/reports/inventory")
    public ResponseEntity<Map<String, Object>> inventoryReport(
            @AuthenticationPrincipal(expression = "user") User admin) {
        return ResponseEntity.ok(administratorPort.inventoryReport(admin));
    }

    @GetMapping("/reports/orders")
    public ResponseEntity<Map<String, Object>> orderReport(
            @AuthenticationPrincipal(expression = "user") User admin) {
        return ResponseEntity.ok(administratorPort.orderReport(admin));
    }

    @GetMapping("/reports/sales")
    public ResponseEntity<Map<String, Object>> salesReport(
            @AuthenticationPrincipal(expression = "user") User admin) {
        return ResponseEntity.ok(administratorPort.salesReport(admin));
    }

    @GetMapping("/reports/audit")
    public ResponseEntity<List<AdminDtos.AuditLogResponse>> consultAudit(
            @AuthenticationPrincipal(expression = "user") User admin,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(administratorPort.consultAudit(admin, limit).stream()
                .map(RestMapper::toAuditLogResponse).toList());
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<InventoryDtos.InventoryResponse>> consultInventory(
            @AuthenticationPrincipal(expression = "user") User admin) {
        return ResponseEntity.ok(administratorPort.consultInventory(admin).stream()
                .map(RestMapper::toInventoryResponse).toList());
    }
}
