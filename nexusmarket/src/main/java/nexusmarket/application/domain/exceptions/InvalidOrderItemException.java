package nexusmarket.application.domain.exceptions;

public class InvalidOrderItemException extends DomainException {

    public InvalidOrderItemException(String message) {
        super(message);
    }
}
