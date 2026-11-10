package nexusmarket.application.adapters.useCases;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.AuditLog;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.in.SupervisorPort;
import nexusmarket.application.domain.services.inventory.ConsultInventoryService;
import nexusmarket.application.domain.services.order.ConsultOrderService;
import nexusmarket.application.domain.services.reports.ReportService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SupervisorUseCaseImpl implements SupervisorPort {

    private final ReportService reportService;
    private final ConsultOrderService consultOrderService;
    private final ConsultInventoryService consultInventoryService;

    @Override
    public Map<String, Object> inventoryReport(User user) {
        return reportService.inventoryReport(user);
    }

    @Override
    public Map<String, Object> orderReport(User user) {
        return reportService.orderReport(user);
    }

    @Override
    public Map<String, Object> salesReport(User user) {
        return reportService.salesReport(user);
    }

    @Override
    public List<AuditLog> consultAudit(User user, int limit) {
        return reportService.consultAudit(user, limit);
    }

    @Override
    public List<Order> consultOrders(User user) {
        return consultOrderService.consultAll(user);
    }

    @Override
    public List<Inventory> consultInventory(User user) {
        return consultInventoryService.consultAll(user);
    }
}
