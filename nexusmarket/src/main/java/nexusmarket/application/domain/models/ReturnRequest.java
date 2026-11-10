package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.enums.ApprovalDecision;
import nexusmarket.application.domain.exceptions.InvalidApprovalDecisionException;
import nexusmarket.application.domain.exceptions.InvalidReturnException;
import nexusmarket.application.domain.exceptions.InvalidReturnStateException;
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

    public void addItem(ReturnItem item) {
        if (item == null) {
            throw new InvalidReturnException("El item de devolucion es obligatorio.");
        }
        this.items.add(item);
    }

    public void applyDecision(ApprovalDecision decision) {
        if (decision == null) {
            throw new InvalidApprovalDecisionException("La decision de aprobacion es obligatoria.");
        }
        switch (decision) {
            case APPROVED -> approve();
            case REJECTED -> reject();
        }
    }

    public void approve() {
        if (!ReturnState.REQUESTED.equals(this.state)) {
            throw new InvalidReturnStateException("Solo una devolucion solicitada puede aprobarse.");
        }
        this.state = ReturnState.APPROVED;
    }

    public void reject() {
        if (!ReturnState.REQUESTED.equals(this.state)) {
            throw new InvalidReturnStateException("Solo una devolucion solicitada puede rechazarse.");
        }
        this.state = ReturnState.REJECTED;
    }

    public void receive() {
        if (!ReturnState.APPROVED.equals(this.state)) {
            throw new InvalidReturnStateException("Solo una devolucion aprobada puede recibirse.");
        }
        this.state = ReturnState.RECEIVED;
    }

    public void complete() {
        if (!ReturnState.RECEIVED.equals(this.state)) {
            throw new InvalidReturnStateException("Solo una devolucion recibida puede completarse.");
        }
        this.state = ReturnState.COMPLETED;
    }
}
