package nexusmarket.application.adapters.useCases;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.AuditLog;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.Refund;
import nexusmarket.application.domain.models.ReturnRequest;
import nexusmarket.application.domain.models.Seller;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.models.Warehouse;
import nexusmarket.application.domain.ports.in.AdministratorPort;
import nexusmarket.application.domain.services.catalog.ConsultProductService;
import nexusmarket.application.domain.services.catalog.ProductStateService;
import nexusmarket.application.domain.services.inventory.ConsultInventoryService;
import nexusmarket.application.domain.services.order.ConsultOrderService;
import nexusmarket.application.domain.services.refund.RefundService;
import nexusmarket.application.domain.services.reports.ReportService;
import nexusmarket.application.domain.services.returns.ReturnService;
import nexusmarket.application.domain.services.seller.ConsultSellerService;
import nexusmarket.application.domain.services.seller.RegisterSellerService;
import nexusmarket.application.domain.services.seller.UpdateSellerService;
import nexusmarket.application.domain.services.user.ManageUserService;
import nexusmarket.application.domain.services.user.RegisterUserService;
import nexusmarket.application.domain.services.warehouse.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdministratorUseCaseImpl implements AdministratorPort {

    private final ManageUserService manageUserService;
    private final RegisterUserService registerUserService;
    private final ConsultSellerService consultSellerService;
    private final RegisterSellerService registerSellerService;
    private final UpdateSellerService updateSellerService;
    private final WarehouseService warehouseService;
    private final ConsultProductService consultProductService;
    private final ProductStateService productStateService;
    private final ConsultOrderService consultOrderService;
    private final ReturnService returnService;
    private final RefundService refundService;
    private final ReportService reportService;
    private final ConsultInventoryService consultInventoryService;

    @Override
    public List<User> consultUsers(User admin) {
        return manageUserService.consultAll(admin);
    }

    @Override
    public User consultUser(User admin, String userId) {
        return manageUserService.consultById(admin, userId);
    }

    @Override
    public User registerUser(User admin, User newUser) {
        return registerUserService.register(newUser);
    }

    @Override
    public User updateUser(User admin, User user) {
        return manageUserService.update(admin, user);
    }

    @Override
    public User blockUser(User admin, String userId) {
        return manageUserService.block(admin, userId);
    }

    @Override
    public User activateUser(User admin, String userId) {
        return manageUserService.activate(admin, userId);
    }

    @Override
    public List<Seller> consultSellers(User admin) {
        return consultSellerService.consultAll(admin);
    }

    @Override
    public Seller consultSeller(User admin, String sellerId) {
        return consultSellerService.consultById(admin, sellerId);
    }

    @Override
    public Seller registerSeller(User admin, Seller seller, Warehouse firstWarehouse) {
        return registerSellerService.register(admin, seller, firstWarehouse);
    }

    @Override
    public Seller updateSeller(User admin, Seller seller) {
        return updateSellerService.updateByAdmin(admin, seller);
    }

    @Override
    public List<Warehouse> consultWarehouses(User admin) {
        return warehouseService.consultAll(admin);
    }

    @Override
    public Warehouse registerWarehouse(User admin, Warehouse warehouse) {
        return warehouseService.register(admin, warehouse);
    }

    @Override
    public Warehouse updateWarehouse(User admin, Warehouse warehouse) {
        return warehouseService.update(admin, warehouse);
    }

    @Override
    public void deleteWarehouse(User admin, String warehouseId) {
        warehouseService.delete(admin, warehouseId);
    }

    @Override
    public List<Product> consultProducts(User admin) {
        return consultProductService.consultAll(admin);
    }

    @Override
    public Product suspendProduct(User admin, String productId) {
        return productStateService.suspend(admin, productId);
    }

    @Override
    public Product discontinueProduct(User admin, String productId) {
        return productStateService.discontinue(admin, productId);
    }

    @Override
    public List<Order> consultOrders(User admin) {
        return consultOrderService.consultAll(admin);
    }

    @Override
    public Order consultOrder(User admin, String orderId) {
        return consultOrderService.consultById(admin, orderId);
    }

    @Override
    public List<ReturnRequest> consultReturns(User admin) {
        return returnService.consultAll(admin);
    }

    @Override
    public ReturnRequest approveReturn(User admin, String returnId) {
        return returnService.approve(admin, returnId);
    }

    @Override
    public ReturnRequest rejectReturn(User admin, String returnId) {
        return returnService.reject(admin, returnId);
    }

    @Override
    public ReturnRequest receiveReturn(User admin, String returnId) {
        return returnService.receive(admin, returnId);
    }

    @Override
    public List<Refund> consultRefunds(User admin) {
        return refundService.consultAll(admin);
    }

    @Override
    public Refund processRefund(User admin, String refundId) {
        return refundService.process(admin, refundId);
    }

    @Override
    public Map<String, Object> inventoryReport(User admin) {
        return reportService.inventoryReport(admin);
    }

    @Override
    public Map<String, Object> orderReport(User admin) {
        return reportService.orderReport(admin);
    }

    @Override
    public Map<String, Object> salesReport(User admin) {
        return reportService.salesReport(admin);
    }

    @Override
    public List<AuditLog> consultAudit(User admin, int limit) {
        return reportService.consultAudit(admin, limit);
    }

    @Override
    public List<Inventory> consultInventory(User admin) {
        return consultInventoryService.consultAll(admin);
    }
}
