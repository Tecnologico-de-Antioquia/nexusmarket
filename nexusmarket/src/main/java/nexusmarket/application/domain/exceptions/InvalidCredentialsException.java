package nexusmarket.application.domain.exceptions;

public class InvalidCredentialsException extends DomainException {

    public InvalidCredentialsException() {
        super("Credenciales invalidas.");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
