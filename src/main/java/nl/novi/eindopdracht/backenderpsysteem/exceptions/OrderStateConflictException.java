package nl.novi.eindopdracht.backenderpsysteem.exceptions;

public class OrderStateConflictException extends RuntimeException {
    public OrderStateConflictException(String message) {
        super(message);
    }
}
