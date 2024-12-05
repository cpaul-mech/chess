package websocket.messages;

public class NotificationMessage extends ServerMessage {
    private String msg;

    public NotificationMessage(String msg) {
        super(ServerMessageType.NOTIFICATION);
        this.msg = msg;
    }
}
