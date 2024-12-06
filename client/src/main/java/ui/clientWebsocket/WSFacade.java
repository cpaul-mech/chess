package ui.clientWebsocket;

import com.google.gson.Gson;
import server.websocket.Connection;
import ui.ServerException;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WSFacade extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;
    //all I need to transfer is the gameID.
    public Integer gameID;


    public WSFacade(String url, NotificationHandler notificationHandler) throws ServerException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    //TODO: IMPLEMENT A SWITCH STATEMENT THAT CHECKS WHAT KIND OF THING IS COMING IN!
                    //then calls notify on that resulting logical step.
                }
            });

            this.notificationHandler = notificationHandler;
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ServerException(ex.getMessage(), 500);
        }

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


}
