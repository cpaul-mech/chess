package service;

public class UnauthorizedAccessError extends RuntimeException {
    public UnauthorizedAccessError(String message) {
        super(message);
    }
}
