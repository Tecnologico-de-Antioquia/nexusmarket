package nexusmarket.application.adapters.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
public class InvoiceEntity {

    @Id
    private String id;

    @Column(unique = true)
    private String orderId;

    private String buyerId;

    @Column(unique = true)
    private String number;

    private LocalDateTime issuedAt;
    private BigDecimal total;
    private String state;
}
