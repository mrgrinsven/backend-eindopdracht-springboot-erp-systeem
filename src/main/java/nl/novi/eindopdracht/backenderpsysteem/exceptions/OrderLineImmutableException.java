package nl.novi.eindopdracht.backenderpsysteem.exceptions;

public class OrderLineImmutableException extends RuntimeException {
    public OrderLineImmutableException(String message) {
        super(message);
    }
}
