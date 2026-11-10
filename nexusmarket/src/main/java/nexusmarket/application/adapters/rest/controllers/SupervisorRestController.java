package nexusmarket.application.adapters.rest.controllers;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.rest.dtos.AdminDtos;
import nexusmarket.application.adapters.rest.dtos.InventoryDtos;
import nexusmarket.application.adapters.rest.dtos.OrderDtos;
import nexusmarket.application.adapters.rest.mappers.RestMapper;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.in.SupervisorPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/supervisor")
@RequiredArgsConstructor
public class SupervisorRestController {

    private final SupervisorPort supervisorPort;

    @GetMapping("/reports/inventory")
    public ResponseEntity<Map<String, Object>> inventoryReport(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(supervisorPort.inventoryReport(user));
    }

    @GetMapping("/reports/orders")
    public ResponseEntity<Map<String, Object>> orderReport(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(supervisorPort.orderReport(user));
    }

    @GetMapping("/reports/sales")
    public ResponseEntity<Map<String, Object>> salesReport(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(supervisorPort.salesReport(user));
    }

    @GetMapping("/reports/audit")
    public ResponseEntity<List<AdminDtos.AuditLogResponse>> consultAudit(
            @AuthenticationPrincipal(expression = "user") User user,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(supervisorPort.consultAudit(user, limit).stream()
                .map(RestMapper::toAuditLogResponse).toList());
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDtos.OrderResponse>> consultOrders(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(supervisorPort.consultOrders(user).stream()
                .map(RestMapper::toOrderResponse).toList());
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<InventoryDtos.InventoryResponse>> consultInventory(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(supervisorPort.consultInventory(user).stream()
                .map(RestMapper::toInventoryResponse).toList());
    }
}
