package nexusmarket.application.adapters.rest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public final class CatalogDtos {

    private CatalogDtos() {
    }

    public record ProductRequest(
            @NotBlank @Size(max = 120) String name,
            @Size(max = 1000) String description,
            @PositiveOrZero BigDecimal price,
            @NotBlank String type,
            String color,
            Integer size,
            String model,
            String format,
            String downloadUrl) {
    }

    public record ProductResponse(String id, String name, String description, BigDecimal price, String type,
                                  String state, String sellerId, String color, Integer size, String model,
                                  String format, String downloadUrl) {
    }
}
