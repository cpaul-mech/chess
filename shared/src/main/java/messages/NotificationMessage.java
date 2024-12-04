package messages;

public class NotificationMessage extends ServerMessage {
    private String notMsg;

    public NotificationMessage(String msg) {
        super(ServerMessageType.NOTIFICATION);
        notMsg = msg;
    }
}
