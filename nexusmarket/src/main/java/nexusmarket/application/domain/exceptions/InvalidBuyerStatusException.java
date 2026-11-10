package nexusmarket.application.domain.exceptions;

public class InvalidBuyerStatusException extends DomainException {

    public InvalidBuyerStatusException(String message) {
        super(message);
    }
}
