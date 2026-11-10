package nexusmarket.application.domain.services.reports;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.AuditLog;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.AuditRepositoryPort;
import nexusmarket.application.domain.ports.out.InventoryRepositoryPort;
import nexusmarket.application.domain.ports.out.OrderRepositoryPort;
import nexusmarket.application.domain.services.security.AuthorizationService;
import nexusmarket.application.domain.valueobjects.OrderState;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final AuditRepositoryPort auditRepositoryPort;
    private final AuthorizationService authorizationService;

    public Map<String, Object> inventoryReport(User user) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SUPERVISOR);
        List<Inventory> inventories = inventoryRepositoryPort.findAll();
        int totalAvailable = inventories.stream()
                .mapToInt(inventory -> inventory.getAvailableQuantity() == null ? 0 : inventory.getAvailableQuantity())
                .sum();
        int totalReserved = inventories.stream()
                .mapToInt(inventory -> inventory.getReservedQuantity() == null ? 0 : inventory.getReservedQuantity())
                .sum();
        int totalDamaged = inventories.stream()
                .mapToInt(inventory -> inventory.getDamagedQuantity() == null ? 0 : inventory.getDamagedQuantity())
                .sum();
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("totalInventoryRecords", inventories.size());
        report.put("totalAvailable", totalAvailable);
        report.put("totalReserved", totalReserved);
        report.put("totalDamaged", totalDamaged);
        return report;
    }

    public Map<String, Object> orderReport(User user) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SUPERVISOR);
        List<Order> orders = orderRepositoryPort.findAll();
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("totalOrders", orders.size());
        for (OrderState state : OrderState.values()) {
            long count = orders.stream().filter(order -> state.equals(order.getState())).count();
            report.put(state.getCode(), count);
        }
        return report;
    }

    public Map<String, Object> salesReport(User user) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SUPERVISOR);
        List<Order> orders = orderRepositoryPort.findAll();
        BigDecimal totalRevenue = orders.stream()
                .filter(order -> OrderState.PAID.equals(order.getState())
                        || OrderState.DISPATCHED.equals(order.getState())
                        || OrderState.DELIVERED.equals(order.getState()))
                .map(order -> order.getTotalAmount() == null ? BigDecimal.ZERO : order.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long paidOrders = orders.stream()
                .filter(order -> OrderState.PAID.equals(order.getState())
                        || OrderState.DISPATCHED.equals(order.getState())
                        || OrderState.DELIVERED.equals(order.getState()))
                .count();
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("paidOrders", paidOrders);
        report.put("totalRevenue", totalRevenue);
        return report;
    }

    public List<AuditLog> consultAudit(User user, int limit) {
        authorizationService.requireAnyRole(user, SystemRole.ADMINISTRATOR, SystemRole.SUPERVISOR);
        int safeLimit = limit <= 0 ? 10 : limit;
        return auditRepositoryPort.findAll().stream()
                .sorted(Comparator.comparing(AuditLog::getOperationDate,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(safeLimit)
                .toList();
    }
}
