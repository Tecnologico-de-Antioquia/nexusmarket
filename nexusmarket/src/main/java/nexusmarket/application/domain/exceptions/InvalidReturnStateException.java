package nexusmarket.application.domain.exceptions;

public class InvalidReturnStateException extends InvalidStateTransitionException {

    public InvalidReturnStateException(String message) {
        super(message);
    }
}
