package nexusmarket.application.domain.exceptions;

public class InvalidPaymentStateException extends DomainException {

    public InvalidPaymentStateException(String message) {
        super(message);
    }
}
