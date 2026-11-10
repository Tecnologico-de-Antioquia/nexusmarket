package nexusmarket.application.adapters.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nexusmarket.application.adapters.rest.dtos.BuyerDtos;
import nexusmarket.application.adapters.rest.dtos.CartDtos;
import nexusmarket.application.adapters.rest.dtos.CatalogDtos;
import nexusmarket.application.adapters.rest.dtos.InvoiceDtos;
import nexusmarket.application.adapters.rest.dtos.OrderDtos;
import nexusmarket.application.adapters.rest.dtos.ReturnDtos;
import nexusmarket.application.adapters.rest.mappers.RestMapper;
import nexusmarket.application.domain.models.Cart;
import nexusmarket.application.domain.models.Invoice;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.Refund;
import nexusmarket.application.domain.models.ReturnRequest;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.in.BuyerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buyer")
@RequiredArgsConstructor
public class BuyerRestController {

    private final BuyerPort buyerPort;

    @GetMapping("/profile")
    public ResponseEntity<BuyerDtos.BuyerResponse> consultMyProfile(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(RestMapper.toBuyerResponse(buyerPort.consultMyProfile(user)));
    }

    @PutMapping("/profile")
    public ResponseEntity<BuyerDtos.BuyerResponse> updateMyProfile(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody BuyerDtos.UpdateRequest request) {
        return ResponseEntity.ok(RestMapper.toBuyerResponse(
                buyerPort.updateMyProfile(user, RestMapper.toDomain(request))));
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<BuyerDtos.AddressResponse>> consultAddresses(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(buyerPort.consultAddresses(user).stream()
                .map(RestMapper::toAddressResponse).toList());
    }

    @PostMapping("/addresses")
    public ResponseEntity<BuyerDtos.BuyerResponse> addAddress(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody BuyerDtos.AddressRequest request) {
        buyerPort.addAddress(user, RestMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestMapper.toBuyerResponse(buyerPort.consultMyProfile(user)));
    }

    @GetMapping("/catalog")
    public ResponseEntity<List<CatalogDtos.ProductResponse>> consultCatalog(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(buyerPort.consultCatalog(user).stream()
                .map(RestMapper::toProductResponse).toList());
    }

    @GetMapping("/catalog/{productId}")
    public ResponseEntity<CatalogDtos.ProductResponse> consultProduct(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String productId) {
        Product product = buyerPort.consultProduct(user, productId);
        return ResponseEntity.ok(RestMapper.toProductResponse(product));
    }

    @GetMapping("/cart")
    public ResponseEntity<CartDtos.CartResponse> consultMyCart(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(RestMapper.toCartResponse(buyerPort.consultMyCart(user)));
    }

    @PostMapping("/cart/items")
    public ResponseEntity<CartDtos.CartResponse> addItem(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody CartDtos.CartItemRequest request) {
        Cart cart = buyerPort.addProductToCart(user, request.productId(), request.quantity());
        return ResponseEntity.ok(RestMapper.toCartResponse(cart));
    }

    @PutMapping("/cart/items/{productId}")
    public ResponseEntity<CartDtos.CartResponse> updateItem(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String productId,
            @Valid @RequestBody CartDtos.CartItemRequest request) {
        Cart cart = buyerPort.updateCartItem(user, productId, request.quantity());
        return ResponseEntity.ok(RestMapper.toCartResponse(cart));
    }

    @DeleteMapping("/cart/items/{productId}")
    public ResponseEntity<CartDtos.CartResponse> removeItem(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String productId) {
        return ResponseEntity.ok(RestMapper.toCartResponse(buyerPort.removeCartItem(user, productId)));
    }

    @DeleteMapping("/cart")
    public ResponseEntity<CartDtos.CartResponse> clearCart(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(RestMapper.toCartResponse(buyerPort.clearMyCart(user)));
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderDtos.OrderResponse> checkout(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody OrderDtos.CheckoutRequest request) {
        Order order = buyerPort.checkout(user, RestMapper.toDomain(request.destinationAddress()));
        return ResponseEntity.status(HttpStatus.CREATED).body(RestMapper.toOrderResponse(order));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDtos.OrderResponse>> consultMyOrders(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(buyerPort.consultMyOrders(user).stream()
                .map(RestMapper::toOrderResponse).toList());
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDtos.OrderResponse> consultOrder(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String orderId) {
        return ResponseEntity.ok(RestMapper.toOrderResponse(buyerPort.consultOrder(user, orderId)));
    }

    @PostMapping("/orders/{orderId}/pay")
    public ResponseEntity<OrderDtos.OrderResponse> payOrder(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String orderId) {
        return ResponseEntity.ok(RestMapper.toOrderResponse(buyerPort.payOrder(user, orderId)));
    }

    @PostMapping("/orders/{orderId}/cancel")
    public ResponseEntity<OrderDtos.OrderResponse> cancelOrder(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String orderId) {
        return ResponseEntity.ok(RestMapper.toOrderResponse(buyerPort.cancelOrder(user, orderId)));
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<InvoiceDtos.InvoiceResponse>> consultMyInvoices(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(buyerPort.consultMyInvoices(user).stream()
                .map(RestMapper::toInvoiceResponse).toList());
    }

    @GetMapping("/invoices/{invoiceId}")
    public ResponseEntity<InvoiceDtos.InvoiceResponse> consultInvoice(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable String invoiceId) {
        Invoice invoice = buyerPort.consultInvoice(user, invoiceId);
        return ResponseEntity.ok(RestMapper.toInvoiceResponse(invoice));
    }

    @GetMapping("/returns")
    public ResponseEntity<List<ReturnDtos.ReturnResponse>> consultMyReturns(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(buyerPort.consultMyReturns(user).stream()
                .map(RestMapper::toReturnResponse).toList());
    }

    @PostMapping("/returns")
    public ResponseEntity<ReturnDtos.ReturnResponse> requestReturn(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody ReturnDtos.CreateReturnRequest request) {
        ReturnRequest returnRequest = buyerPort.requestReturn(user, request.orderId(),
                RestMapper.toReturnItems(request.items()), request.reason());
        return ResponseEntity.status(HttpStatus.CREATED).body(RestMapper.toReturnResponse(returnRequest));
    }

    @GetMapping("/refunds")
    public ResponseEntity<List<ReturnDtos.RefundResponse>> consultMyRefunds(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(buyerPort.consultMyRefunds(user).stream()
                .map(RestMapper::toRefundResponse).toList());
    }
}
