package nexusmarket.application.adapters.persistence.jpa.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class OrderEntity {

    @Id
    private String id;
    private String buyerId;
    private String buyerEmail;
    private String buyerFullName;
    private String state;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime dispatchedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
    private String invoiceId;
    private String transactionId;
    private String shipmentId;
    private String returnRequestId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItemEntity> items = new ArrayList<>();
}
