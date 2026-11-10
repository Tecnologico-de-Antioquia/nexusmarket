package nexusmarket.application.adapters.persistence.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class ProductEntity {

    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String sellerId;
    private String state;
    private String productType;
    private String color;
    private Integer size;
    private String model;
    private String format;
    private String downloadUrl;
}
