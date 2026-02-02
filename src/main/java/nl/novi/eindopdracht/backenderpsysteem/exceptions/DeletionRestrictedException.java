package nl.novi.eindopdracht.backenderpsysteem.exceptions;

public class DeletionRestrictedException extends RuntimeException {
    public DeletionRestrictedException(String message) {
        super(message);
    }
}
