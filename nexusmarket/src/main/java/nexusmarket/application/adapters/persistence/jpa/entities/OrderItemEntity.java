package nexusmarket.application.adapters.persistence.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItemEntity {

    @Id
    private String id;
    private String productId;
    private String productName;
    private String sellerId;
    private String productType;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal subtotal;
}
