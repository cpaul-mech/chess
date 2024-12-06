package ui.clientWebsocket;

import websocket.messages.NotificationMessage;

public interface NotificationHandler {
    void notify(NotificationMessage notificationMessage);
}
