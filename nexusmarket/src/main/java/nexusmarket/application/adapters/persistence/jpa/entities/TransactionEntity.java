package nexusmarket.application.adapters.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class TransactionEntity {

    @Id
    private String id;

    @Column(unique = true)
    private String orderId;

    private String buyerId;
    private BigDecimal amount;
    private String state;
    private String providerReference;
}
