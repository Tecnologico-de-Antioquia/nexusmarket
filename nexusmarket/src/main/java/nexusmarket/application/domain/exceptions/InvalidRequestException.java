package nexusmarket.application.domain.exceptions;

public class InvalidRequestException extends DomainException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
