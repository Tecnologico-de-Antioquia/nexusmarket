package nexusmarket.application.domain.exceptions;

public class InvalidStateTransitionException extends DomainException {

    public InvalidStateTransitionException(String message) {
        super(message);
    }
}
