package service;

public class BadServiceRequest extends RuntimeException {
    public BadServiceRequest(String message) {
        super(message);
    }
}
