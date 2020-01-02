package logic.exceptions;

public class EventConflictException extends RuntimeException {
    public EventConflictException(String coincidentEventName) {
        super("Conflict: "+coincidentEventName);
    }
}
