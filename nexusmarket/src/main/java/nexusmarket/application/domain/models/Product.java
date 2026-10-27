package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.Setter;
import nexusmarket.application.domain.valueObjects.ProductState;

@Getter
@Setter
public abstract class Product {
    private String id;
    private String name;
    private String description;
    private ProductState state;
    private String sellerId;

    protected Product(String id, String name, String description, String sellerId) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product id is required");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (sellerId == null || sellerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Seller id is required");
        }

        this.id = id.trim();
        this.name = name.trim();
        this.description = description;
        this.sellerId = sellerId.trim();
        this.state = ProductState.PUBLISHED;
    }

    public void suspend() {
        this.state = ProductState.SUSPENDED;
    }

    public void publish() {
        this.state = ProductState.PUBLISHED;
    }

    public void discontinue() {
        this.state = ProductState.DISCONTINUED;
    }
}
