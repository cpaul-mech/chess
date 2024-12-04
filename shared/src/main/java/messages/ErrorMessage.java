package messages;

public class ErrorMessage extends ServerMessage {
    private String errorMsg;

    public ErrorMessage(String msg) {
        super(ServerMessageType.ERROR);
        errorMsg = msg;
    }
}
