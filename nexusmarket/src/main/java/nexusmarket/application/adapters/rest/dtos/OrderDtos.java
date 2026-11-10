package nexusmarket.application.adapters.rest.dtos;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class OrderDtos {

    private OrderDtos() {
    }

    public record CheckoutRequest(@Valid BuyerDtos.AddressRequest destinationAddress) {
    }

    public record OrderItemResponse(String id, String productId, String productName, int quantity, BigDecimal unitPrice,
                                    BigDecimal subtotal) {
    }

    public record OrderResponse(String id, String buyerId, String state, BigDecimal totalAmount,
                                LocalDateTime createdAt, LocalDateTime paidAt, LocalDateTime dispatchedAt,
                                LocalDateTime deliveredAt, List<OrderItemResponse> items, String invoiceId,
                                String shipmentId) {
    }
}
