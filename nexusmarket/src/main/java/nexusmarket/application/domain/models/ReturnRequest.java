package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.exceptions.InvalidStateTransitionException;
import nexusmarket.application.domain.valueobjects.ReturnState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReturnRequest {

    private String id;
    private Order order;
    private List<ReturnItem> items = new ArrayList<>();
    private String reason;
    private ReturnState state = ReturnState.REQUESTED;
    private LocalDateTime requestedAt = LocalDateTime.now();

    public void approve() {
        if (!ReturnState.REQUESTED.equals(this.state)) {
            throw new InvalidStateTransitionException("Solo una devolucion solicitada puede aprobarse.");
        }
        this.state = ReturnState.APPROVED;
    }

    public void reject() {
        if (!ReturnState.REQUESTED.equals(this.state)) {
            throw new InvalidStateTransitionException("Solo una devolucion solicitada puede rechazarse.");
        }
        this.state = ReturnState.REJECTED;
    }

    public void receive() {
        if (!ReturnState.APPROVED.equals(this.state)) {
            throw new InvalidStateTransitionException("Solo una devolucion aprobada puede recibirse.");
        }
        this.state = ReturnState.RECEIVED;
    }

    public void complete() {
        if (!ReturnState.RECEIVED.equals(this.state)) {
            throw new InvalidStateTransitionException("Solo una devolucion recibida puede completarse.");
        }
        this.state = ReturnState.COMPLETED;
    }
}
