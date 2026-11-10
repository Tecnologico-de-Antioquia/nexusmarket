package nexusmarket.application.adapters.rest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public final class CartDtos {

    private CartDtos() {
    }

    public record CartItemRequest(@NotBlank String productId, @Positive int quantity) {
    }

    public record CartItemResponse(String productId, String productName, int quantity, BigDecimal unitPrice) {
    }

    public record CartResponse(String id, String buyerId, String state, List<CartItemResponse> items) {
    }
}
