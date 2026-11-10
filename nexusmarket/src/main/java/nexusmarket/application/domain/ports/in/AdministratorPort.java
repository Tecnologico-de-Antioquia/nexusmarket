package nexusmarket.application.domain.ports.in;

import nexusmarket.application.domain.models.AuditLog;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.Refund;
import nexusmarket.application.domain.models.ReturnRequest;
import nexusmarket.application.domain.models.Seller;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.models.Warehouse;

import java.util.List;
import java.util.Map;

public interface AdministratorPort {

    List<User> consultUsers(User admin);

    User consultUser(User admin, String userId);

    User registerUser(User admin, User newUser);

    User updateUser(User admin, User user);

    User blockUser(User admin, String userId);

    User activateUser(User admin, String userId);

    List<Seller> consultSellers(User admin);

    Seller consultSeller(User admin, String sellerId);

    Seller registerSeller(User admin, Seller seller, Warehouse firstWarehouse);

    Seller updateSeller(User admin, Seller seller);

    List<Warehouse> consultWarehouses(User admin);

    Warehouse registerWarehouse(User admin, Warehouse warehouse);

    Warehouse updateWarehouse(User admin, Warehouse warehouse);

    void deleteWarehouse(User admin, String warehouseId);

    List<Product> consultProducts(User admin);

    Product suspendProduct(User admin, String productId);

    Product discontinueProduct(User admin, String productId);

    List<Order> consultOrders(User admin);

    Order consultOrder(User admin, String orderId);

    List<ReturnRequest> consultReturns(User admin);

    ReturnRequest approveReturn(User admin, String returnId);

    ReturnRequest rejectReturn(User admin, String returnId);

    ReturnRequest receiveReturn(User admin, String returnId);

    List<Refund> consultRefunds(User admin);

    Refund processRefund(User admin, String refundId);

    Map<String, Object> inventoryReport(User admin);

    Map<String, Object> orderReport(User admin);

    Map<String, Object> salesReport(User admin);

    List<AuditLog> consultAudit(User admin, int limit);

    List<Inventory> consultInventory(User admin);
}
