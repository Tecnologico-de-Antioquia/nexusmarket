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
@Table(name = "refunds")
@Getter
@Setter
@NoArgsConstructor
public class RefundEntity {

    @Id
    private String id;

    @Column(unique = true)
    private String returnId;

    private BigDecimal amount;
    private String state;
    private LocalDateTime processedAt;
}
