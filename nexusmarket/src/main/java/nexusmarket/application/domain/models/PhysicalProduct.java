package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.Setter;
import nexusmarket.application.domain.valueobjects.ProductType;

@Getter
@Setter
public class PhysicalProduct extends Product {

    private Variant variant;

    public PhysicalProduct() {
        setProductType(ProductType.PHYSICAL);
    }
}
