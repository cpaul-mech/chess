package websocket.messages;

public class ErrorMessage extends ServerMessage {
    private String errorMessage;

    public ErrorMessage(String msg) {
        super(ServerMessageType.ERROR);
        errorMessage = msg;
    }
}