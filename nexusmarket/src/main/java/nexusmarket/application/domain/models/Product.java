package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.valueobjects.ProductState;
import nexusmarket.application.domain.valueobjects.ProductType;

@Getter
@Setter
@NoArgsConstructor
public abstract class Product {

    private String id;
    private String name;
    private String description;
    private java.math.BigDecimal price = java.math.BigDecimal.ZERO;
    private Seller seller;
    private ProductState state = ProductState.PUBLISHED;
    private ProductType productType;

    public void publish() {
        if (ProductState.DISCONTINUED.equals(state)) {
            throw new IllegalStateException("Un producto descontinuado no puede publicarse nuevamente.");
        }
        this.state = ProductState.PUBLISHED;
    }

    public void suspend() {
        this.state = ProductState.SUSPENDED;
    }

    public void discontinue() {
        this.state = ProductState.DISCONTINUED;
    }

    public boolean isPublished() {
        return ProductState.PUBLISHED.equals(state);
    }
}
