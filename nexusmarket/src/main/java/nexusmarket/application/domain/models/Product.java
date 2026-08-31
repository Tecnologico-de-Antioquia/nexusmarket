package nexusmarket.application.domain.models;

import nexusmarket.application.domain.valueObjects.ProductState;

public abstract class Product {
    private Integer id;
    private String name;
    private Variant variant;
    private ProductState state;
}
