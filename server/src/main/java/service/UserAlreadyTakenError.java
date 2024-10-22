package service;

public class UserAlreadyTakenError extends RuntimeException {
    public UserAlreadyTakenError(String message) {
        super(message);
    }
}
