package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.valueobjects.RefundState;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Refund {

    private String id;
    private ReturnRequest returnRequest;
    private BigDecimal amount;
    private RefundState state = RefundState.PENDING;
    private LocalDateTime processedAt;

    public void process() {
        this.state = RefundState.PROCESSED;
        this.processedAt = LocalDateTime.now();
    }

    public void fail() {
        this.state = RefundState.FAILED;
        this.processedAt = LocalDateTime.now();
    }
}
