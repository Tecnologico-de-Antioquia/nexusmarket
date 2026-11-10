package nexusmarket.application.adapters.rest.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class ReturnDtos {

    private ReturnDtos() {
    }

    public record ReturnItemRequest(@NotBlank String orderItemId, @Positive int quantity) {
    }

    public record CreateReturnRequest(
            @NotBlank String orderId,
            @NotBlank String reason,
            @NotEmpty @Valid List<ReturnItemRequest> items) {
    }

    public record ReturnItemResponse(String orderItemId, String productId, int quantity) {
    }

    public record ReturnResponse(String id, String orderId, String reason, String state,
                                 LocalDateTime requestedAt, List<ReturnItemResponse> items) {
    }

    public record RefundResponse(String id, String returnId, BigDecimal amount, String state,
                                 LocalDateTime processedAt) {
    }
}
