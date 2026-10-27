package nexusmarket.application.domain.services.reports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.ports.out.AuditRepositoryPort;
import nexusmarket.application.domain.ports.out.InventoryRepositoryPort;
import nexusmarket.application.domain.ports.out.OrderRepositoryPort;

@Service
@RequiredArgsConstructor
public class ReportsService {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final AuditRepositoryPort auditRepositoryPort;

    public Map<String, Long> inventorySummary() {
        Map<String, Long> summary = new HashMap<>();
        List<Inventory> inventories = inventoryRepositoryPort.findAll();
        summary.put("totalItems", (long) inventories.size());
        summary.put("totalAvailable", inventories.stream().mapToLong(Inventory::getAvailableQuantity).sum());
        summary.put("totalReserved", inventories.stream().mapToLong(Inventory::getReservedQuantity).sum());
        return summary;
    }

    public Map<String, Long> orderSummary() {
        Map<String, Long> summary = new HashMap<>();
        List<Order> orders = orderRepositoryPort.findAll();
        summary.put("totalOrders", (long) orders.size());
        return summary;
    }

    public List<String> recentAuditEvents(int limit) {
        return auditRepositoryPort.findRecentEvents(limit);
    }
}