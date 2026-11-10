package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.exceptions.InvalidOrderItemException;
import nexusmarket.application.domain.valueobjects.Currency;
import nexusmarket.application.domain.valueobjects.Money;

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

    public Money calculateSubtotalMoney(Currency currency) {
        return Money.of(calculateSubtotal(), currency);
    }

    public void recalculateSubtotal() {
        if (quantity == null || quantity <= 0) {
            throw new InvalidOrderItemException("La cantidad del item debe ser mayor que cero.");
        }
        if (unitPrice == null) {
            throw new InvalidOrderItemException("El precio unitario del item es obligatorio.");
        }
        if (unitPrice.signum() < 0) {
            throw new InvalidOrderItemException("El precio unitario del item no puede ser negativo.");
        }
        this.subtotal = calculateSubtotal();
    }
}
