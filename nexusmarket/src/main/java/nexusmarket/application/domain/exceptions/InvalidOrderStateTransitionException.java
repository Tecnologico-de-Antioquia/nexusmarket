package nexusmarket.application.domain.exceptions;

public class InvalidOrderStateTransitionException extends InvalidStateTransitionException {

    public InvalidOrderStateTransitionException(String message) {
        super(message);
    }
}
