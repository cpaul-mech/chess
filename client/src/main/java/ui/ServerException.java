package ui;

public class ServerException extends Exception {
    private int rCode;

    public ServerException(String message, int responseCode) {
        super(message);
        rCode = responseCode;
    }

    public int getrCode() {
        return rCode;
    }
}
