package ui.clientWebsocket;

import chess.ChessMove;
import com.google.gson.Gson;
import model.AuthData;
import ui.ServerException;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WSFacade extends Endpoint {
    private Session session;
    private NotificationHandler notificationHandler;
    private final Gson serializer = new Gson();
    //all I need to transfer is the gameID.


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
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

                    //then calls notify on that resulting logical step.
                    switch (serverMessage.getServerMessageType()) {
                        case NOTIFICATION -> {
                            NotificationMessage notificationMessage = serializer.fromJson(message, NotificationMessage.class);
                            notificationHandler.notify(notificationMessage, null, null);
                        }
                        case LOAD_GAME -> {
                            LoadGameMessage loadGameMessage = serializer.fromJson(message, LoadGameMessage.class);
                            notificationHandler.notify(null, loadGameMessage, null);
                        }
                        case ERROR -> {
                            ErrorMessage errorMessage = serializer.fromJson(message, ErrorMessage.class);
                            notificationHandler.notify(null, null, errorMessage);
                        }
                    }
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


    public void connect(AuthData authData, int gameID) throws ServerException {
        ConnectCommand connectCommand = new ConnectCommand(authData.authToken(), gameID);
        sendMessage(connectCommand);
    }

    public void leave(AuthData authData, int gameID) throws ServerException {
        LeaveCommand leaveCommand = new LeaveCommand(authData.authToken(), gameID);
        sendMessage(leaveCommand);
    }

    public void makeMove(AuthData authData, int gameID, ChessMove move) throws ServerException {
        MakeMoveCommand moveCommand = new MakeMoveCommand(authData.authToken(), gameID, move);
        sendMessage(moveCommand);
    }


    public void sendMessage(UserGameCommand userGameCommand) throws ServerException {
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException ex) {
            throw new ServerException(ex.getMessage(), 500);
        }
    }
}
