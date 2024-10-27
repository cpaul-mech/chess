package exceptions;

public class SQLResponseException extends Exception {
    final private int statusCode;

    public SQLResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int StatusCode() {
        return statusCode;
    }
}
