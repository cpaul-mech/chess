package service;

public class badServiceRequest extends RuntimeException {
    public badServiceRequest(String message) {
        super(message);
    }
}
