package nexusmarket.application.adapters.useCases;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.Seller;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.models.Warehouse;
import nexusmarket.application.domain.ports.in.SellerPort;
import nexusmarket.application.domain.services.catalog.ConsultProductService;
import nexusmarket.application.domain.services.catalog.ProductStateService;
import nexusmarket.application.domain.services.catalog.RegisterProductService;
import nexusmarket.application.domain.services.catalog.UpdateProductService;
import nexusmarket.application.domain.services.inventory.ConsultInventoryService;
import nexusmarket.application.domain.services.inventory.InventoryService;
import nexusmarket.application.domain.services.order.ConsultOrderService;
import nexusmarket.application.domain.services.seller.ConsultSellerService;
import nexusmarket.application.domain.services.seller.UpdateSellerService;
import nexusmarket.application.domain.services.warehouse.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerUseCaseImpl implements SellerPort {

    private final ConsultSellerService consultSellerService;
    private final UpdateSellerService updateSellerService;
    private final ConsultProductService consultProductService;
    private final RegisterProductService registerProductService;
    private final UpdateProductService updateProductService;
    private final ProductStateService productStateService;
    private final WarehouseService warehouseService;
    private final ConsultInventoryService consultInventoryService;
    private final InventoryService inventoryService;
    private final ConsultOrderService consultOrderService;

    @Override
    public Seller consultMyProfile(User user) {
        return consultSellerService.consultMyProfile(user);
    }

    @Override
    public Seller updateMyProfile(User user, Seller seller) {
        return updateSellerService.updateMyProfile(user, seller);
    }

    @Override
    public List<Product> consultMyProducts(User user) {
        return consultProductService.consultBySeller(user);
    }

    @Override
    public Product registerProduct(User user, Product product) {
        return registerProductService.register(user, product);
    }

    @Override
    public Product updateProduct(User user, Product product) {
        return updateProductService.update(user, product);
    }

    @Override
    public Product publishProduct(User user, String productId) {
        return productStateService.publish(user, productId);
    }

    @Override
    public Product suspendProduct(User user, String productId) {
        return productStateService.suspend(user, productId);
    }

    @Override
    public Product discontinueProduct(User user, String productId) {
        return productStateService.discontinue(user, productId);
    }

    @Override
    public List<Warehouse> consultMyWarehouses(User user) {
        return warehouseService.consultBySeller(user);
    }

    @Override
    public List<Inventory> consultMyInventory(User user) {
        return consultInventoryService.consultAll(user);
    }

    @Override
    public Inventory registerInventory(User user, Inventory inventory) {
        return inventoryService.register(user, inventory);
    }

    @Override
    public Inventory receiveStock(User user, String productId, String warehouseId, int quantity) {
        return inventoryService.receive(user, productId, warehouseId, quantity);
    }

    @Override
    public Inventory reserveStock(User user, String productId, String warehouseId, int quantity) {
        return inventoryService.reserve(user, productId, warehouseId, quantity);
    }

    @Override
    public Inventory releaseReservation(User user, String productId, String warehouseId, int quantity) {
        return inventoryService.releaseReservation(user, productId, warehouseId, quantity);
    }

    @Override
    public Inventory registerSaleOut(User user, String productId, String warehouseId, int quantity) {
        return inventoryService.saleOut(user, productId, warehouseId, quantity);
    }

    @Override
    public Inventory adjustInventory(User user, String productId, String warehouseId, int delta) {
        return inventoryService.adjust(user, productId, warehouseId, delta);
    }

    @Override
    public Inventory registerReturn(User user, String productId, String warehouseId, int quantity) {
        return inventoryService.returnStock(user, productId, warehouseId, quantity);
    }

    @Override
    public List<Order> consultMyOrders(User user) {
        return consultOrderService.consultBySeller(user);
    }
}
