package server.websocket;

import com.google.gson.Gson;
import commands.UserGameCommand;
import dataaccess.DataAccessException;
import exceptions.UnauthorizedAccessError;
import messages.ErrorMessage;
import model.AuthData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;

@WebSocket
public class WebsocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final Gson serializer = new Gson();
    private Server server;

    public WebsocketHandler(Server server) {
        this.server = server;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand command = serializer.fromJson(message, UserGameCommand.class);
            var conn = getConnection(command.getAuthToken(), session);
        } catch (Exception ex) {
            sendMessage(session.getRemote(), new ErrorMessage(ex.getMessage()));
        }
    }

    private void sendMessage(RemoteEndpoint remote, ErrorMessage errorMessage) {
        //TODO: What does this method need to include?
    }

    private Connection getConnection(String authToken, Session session) throws DataAccessException {
        //TODO: need username so I can get the right Connection Object.
        String username = wsVerifyAuthToken(authToken);
        if (username != null) {
            return new Connection(username, session);
        } else {
            throw new UnauthorizedAccessError("Error: unauthorized");
        }

    }

    private String wsVerifyAuthToken(String authToken) throws DataAccessException {
        boolean result = server.getHandler().getAuthService().verifyAuthToken(authToken);
        var authService = server.getHandler().getAuthService();
        if (result) {
            return authService.getAuthData(authToken).username();
        } else {
            throw new UnauthorizedAccessError("Error: unauthorized");
        }
    }

}
