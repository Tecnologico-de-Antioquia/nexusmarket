package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    private String id;
    private Product product;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public BigDecimal calculateSubtotal() {
        if (unitPrice == null) {
            return BigDecimal.ZERO;
        }
        int qty = quantity == null ? 0 : quantity;
        return unitPrice.multiply(BigDecimal.valueOf(qty));
    }

    public void recalculateSubtotal() {
        this.subtotal = calculateSubtotal();
    }
}
