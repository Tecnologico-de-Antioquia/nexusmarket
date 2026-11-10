package nexusmarket.application.domain.ports.in;

import nexusmarket.application.domain.models.Address;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.models.Cart;
import nexusmarket.application.domain.models.Invoice;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.Refund;
import nexusmarket.application.domain.models.ReturnItem;
import nexusmarket.application.domain.models.ReturnRequest;
import nexusmarket.application.domain.models.User;

import java.util.List;

public interface BuyerPort {

    Buyer consultMyProfile(User user);

    Buyer updateMyProfile(User user, Buyer buyer);

    Address addAddress(User user, Address address);

    List<Address> consultAddresses(User user);

    List<Product> consultCatalog(User user);

    Product consultProduct(User user, String productId);

    Cart consultMyCart(User user);

    Cart addProductToCart(User user, String productId, int quantity);

    Cart updateCartItem(User user, String productId, int quantity);

    Cart removeCartItem(User user, String productId);

    Cart clearMyCart(User user);

    Order checkout(User user, Address destinationAddress);

    Order payOrder(User user, String orderId);

    Order cancelOrder(User user, String orderId);

    List<Order> consultMyOrders(User user);

    Order consultOrder(User user, String orderId);

    List<Invoice> consultMyInvoices(User user);

    Invoice consultInvoice(User user, String invoiceId);

    ReturnRequest requestReturn(User user, String orderId, List<ReturnItem> items, String reason);

    List<ReturnRequest> consultMyReturns(User user);

    List<Refund> consultMyRefunds(User user);
}
