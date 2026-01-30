package nl.novi.eindopdracht.backenderpsysteem.exceptions;

public class QuantityExceededException extends RuntimeException {
    public QuantityExceededException(String message) {
        super(message);
    }
}
