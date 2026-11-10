package nexusmarket.application.domain.exceptions;

public class InvalidCartStateException extends DomainException {

    public InvalidCartStateException(String message) {
        super(message);
    }
}
