package nexusmarket.application.domain.exceptions;

public class ResourceAlreadyExistsException extends DomainException {

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
