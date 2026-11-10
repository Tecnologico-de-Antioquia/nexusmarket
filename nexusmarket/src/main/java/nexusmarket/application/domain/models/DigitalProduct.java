package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.Setter;
import nexusmarket.application.domain.valueobjects.ProductType;

@Getter
@Setter
public class DigitalProduct extends Product {

    private String format;
    private String downloadUrl;

    public DigitalProduct() {
        setProductType(ProductType.DIGITAL);
    }
}
