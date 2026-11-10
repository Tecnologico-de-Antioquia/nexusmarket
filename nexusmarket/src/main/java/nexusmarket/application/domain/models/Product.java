package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.exceptions.InvalidProductException;
import nexusmarket.application.domain.exceptions.InvalidProductStateException;
import nexusmarket.application.domain.valueobjects.Currency;
import nexusmarket.application.domain.valueobjects.Money;
import nexusmarket.application.domain.valueobjects.ProductState;
import nexusmarket.application.domain.valueobjects.ProductType;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public abstract class Product {

    private String id;
    private String name;
    private String description;
    private BigDecimal price = BigDecimal.ZERO;
    private Seller seller;
    private ProductState state = ProductState.PUBLISHED;
    private ProductType productType;

    public void publish() {
        if (ProductState.DISCONTINUED.equals(state)) {
            throw new InvalidProductStateException("Un producto descontinuado no puede publicarse nuevamente.");
        }
        this.state = ProductState.PUBLISHED;
    }

    public void suspend() {
        if (ProductState.DISCONTINUED.equals(state)) {
            throw new InvalidProductStateException("Un producto descontinuado no puede suspenderse.");
        }
        this.state = ProductState.SUSPENDED;
    }

    public void discontinue() {
        if (ProductState.DISCONTINUED.equals(state)) {
            throw new InvalidProductStateException("El producto ya esta descontinuado.");
        }
        this.state = ProductState.DISCONTINUED;
    }

    public void changePrice(BigDecimal price) {
        if (price == null) {
            throw new InvalidProductException("El precio es obligatorio.");
        }
        if (price.signum() < 0) {
            throw new InvalidProductException("El precio no puede ser negativo.");
        }
        this.price = price;
    }

    public Money priceAsMoney(Currency currency) {
        return Money.of(price == null ? BigDecimal.ZERO : price, currency);
    }

    public boolean isPublished() {
        return ProductState.PUBLISHED.equals(state);
    }

    public boolean isAvailableForSale() {
        return isPublished() && price != null && price.signum() > 0;
    }

    public abstract boolean requiresShipping();

    public abstract boolean requiresInventory();
}
