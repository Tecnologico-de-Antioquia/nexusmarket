package nexusmarket.application.domain.exceptions;

public class InvalidProductStateException extends DomainException {

    public InvalidProductStateException(String message) {
        super(message);
    }
}
