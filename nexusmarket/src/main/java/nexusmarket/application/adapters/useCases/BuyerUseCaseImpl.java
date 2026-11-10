package nexusmarket.application.adapters.useCases;

import lombok.RequiredArgsConstructor;
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
import nexusmarket.application.domain.ports.in.BuyerPort;
import nexusmarket.application.domain.services.billing.InvoiceService;
import nexusmarket.application.domain.services.buyer.AddressService;
import nexusmarket.application.domain.services.buyer.ConsultBuyerService;
import nexusmarket.application.domain.services.buyer.UpdateBuyerService;
import nexusmarket.application.domain.services.cart.CartService;
import nexusmarket.application.domain.services.catalog.ConsultProductService;
import nexusmarket.application.domain.services.order.CancelOrderService;
import nexusmarket.application.domain.services.order.CheckoutService;
import nexusmarket.application.domain.services.order.ConsultOrderService;
import nexusmarket.application.domain.services.order.PayOrderService;
import nexusmarket.application.domain.services.refund.RefundService;
import nexusmarket.application.domain.services.returns.ReturnService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyerUseCaseImpl implements BuyerPort {

    private final ConsultBuyerService consultBuyerService;
    private final UpdateBuyerService updateBuyerService;
    private final AddressService addressService;
    private final ConsultProductService consultProductService;
    private final CartService cartService;
    private final CheckoutService checkoutService;
    private final PayOrderService payOrderService;
    private final CancelOrderService cancelOrderService;
    private final ConsultOrderService consultOrderService;
    private final InvoiceService invoiceService;
    private final ReturnService returnService;
    private final RefundService refundService;

    @Override
    public Buyer consultMyProfile(User user) {
        return consultBuyerService.consultMyProfile(user);
    }

    @Override
    public Buyer updateMyProfile(User user, Buyer buyer) {
        return updateBuyerService.updateMyProfile(user, buyer);
    }

    @Override
    public Address addAddress(User user, Address address) {
        return addressService.addAddress(user, address).getPrimaryAddress();
    }

    @Override
    public List<Address> consultAddresses(User user) {
        return addressService.consultAddresses(user);
    }

    @Override
    public List<Product> consultCatalog(User user) {
        return consultProductService.consultCatalog(user);
    }

    @Override
    public Product consultProduct(User user, String productId) {
        return consultProductService.consultById(user, productId);
    }

    @Override
    public Cart consultMyCart(User user) {
        return cartService.consult(user);
    }

    @Override
    public Cart addProductToCart(User user, String productId, int quantity) {
        return cartService.add(user, productId, quantity);
    }

    @Override
    public Cart updateCartItem(User user, String productId, int quantity) {
        return cartService.update(user, productId, quantity);
    }

    @Override
    public Cart removeCartItem(User user, String productId) {
        return cartService.remove(user, productId);
    }

    @Override
    public Cart clearMyCart(User user) {
        return cartService.clear(user);
    }

    @Override
    public Order checkout(User user, Address destinationAddress) {
        return checkoutService.checkout(user, destinationAddress);
    }

    @Override
    public Order payOrder(User user, String orderId) {
        return payOrderService.pay(user, orderId);
    }

    @Override
    public Order cancelOrder(User user, String orderId) {
        return cancelOrderService.cancel(user, orderId);
    }

    @Override
    public List<Order> consultMyOrders(User user) {
        return consultOrderService.consultMyOrders(user);
    }

    @Override
    public Order consultOrder(User user, String orderId) {
        return consultOrderService.consultMyOrder(user, orderId);
    }

    @Override
    public List<Invoice> consultMyInvoices(User user) {
        return invoiceService.consultMyInvoices(user);
    }

    @Override
    public Invoice consultInvoice(User user, String invoiceId) {
        return invoiceService.consultMyInvoice(user, invoiceId);
    }

    @Override
    public ReturnRequest requestReturn(User user, String orderId, List<ReturnItem> items, String reason) {
        return returnService.request(user, orderId, items, reason);
    }

    @Override
    public List<ReturnRequest> consultMyReturns(User user) {
        return returnService.consultMyReturns(user);
    }

    @Override
    public List<Refund> consultMyRefunds(User user) {
        return refundService.consultMyRefunds(user);
    }
}
