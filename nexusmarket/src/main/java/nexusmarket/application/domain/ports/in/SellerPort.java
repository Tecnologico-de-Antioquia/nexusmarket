package nexusmarket.application.domain.ports.in;

import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.Seller;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.models.Warehouse;

import java.util.List;

public interface SellerPort {

    Seller consultMyProfile(User user);

    Seller updateMyProfile(User user, Seller seller);

    List<Product> consultMyProducts(User user);

    Product registerProduct(User user, Product product);

    Product updateProduct(User user, Product product);

    Product publishProduct(User user, String productId);

    Product suspendProduct(User user, String productId);

    Product discontinueProduct(User user, String productId);

    List<Warehouse> consultMyWarehouses(User user);

    List<Inventory> consultMyInventory(User user);

    Inventory registerInventory(User user, Inventory inventory);

    Inventory receiveStock(User user, String productId, String warehouseId, int quantity);

    Inventory reserveStock(User user, String productId, String warehouseId, int quantity);

    Inventory releaseReservation(User user, String productId, String warehouseId, int quantity);

    Inventory registerSaleOut(User user, String productId, String warehouseId, int quantity);

    Inventory adjustInventory(User user, String productId, String warehouseId, int delta);

    Inventory registerReturn(User user, String productId, String warehouseId, int quantity);

    List<Order> consultMyOrders(User user);
}
