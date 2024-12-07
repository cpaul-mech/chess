package ui.clientWebsocket;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public interface NotificationHandler {
    void notify(NotificationMessage n, LoadGameMessage l, ErrorMessage e);
}
