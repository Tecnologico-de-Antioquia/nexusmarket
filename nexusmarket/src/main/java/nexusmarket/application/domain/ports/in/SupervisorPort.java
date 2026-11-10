package nexusmarket.application.domain.ports.in;

import nexusmarket.application.domain.models.AuditLog;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.User;

import java.util.List;
import java.util.Map;

public interface SupervisorPort {

    Map<String, Object> inventoryReport(User user);

    Map<String, Object> orderReport(User user);

    Map<String, Object> salesReport(User user);

    List<AuditLog> consultAudit(User user, int limit);

    List<Order> consultOrders(User user);

    List<Inventory> consultInventory(User user);
}
