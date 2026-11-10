package nexusmarket.application.domain.exceptions;

public class InvalidShipmentStateException extends InvalidStateTransitionException {

    public InvalidShipmentStateException(String message) {
        super(message);
    }
}
