package server.websocket;

import com.google.gson.Gson;
import commands.ConnectCommand;
import commands.UserGameCommand;
import dataaccess.DataAccessException;
import exceptions.UnauthorizedAccessError;
import messages.ErrorMessage;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;

import java.io.IOException;

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
//        System.out.printf("Received: %s", message);
//        session.getRemote().sendString("WebSocket response: " + message);
        try {
            UserGameCommand command = serializer.fromJson(message, UserGameCommand.class);
            Connection conn = getConnection(command.getAuthToken(), session);
            if (conn != null) {
                switch (command.getCommandType()) {
                    case UserGameCommand.CommandType.CONNECT -> connect(conn, message);
                    case LEAVE -> leave(conn, message);
                    case MAKE_MOVE -> makeMove(conn, message);
                    case RESIGN -> resign(conn, message);
                    case null, default -> {
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            sendMessage(session.getRemote(), new ErrorMessage(ex.getMessage()));
        }

    }

    private void resign(Connection conn, String message) {

    }

    private void makeMove(Connection conn, String message) {
    }

    private void leave(Connection conn, String message) {
    }

    private void connect(Connection conn, String msg) {
        ConnectCommand connectCommand = serializer.fromJson(msg, ConnectCommand.class);
        ErrorMessage e = new ErrorMessage("This is an error message!!");
        sendMessage(conn.session.getRemote(), e);
    }

    public void sendMessage(RemoteEndpoint remote, ErrorMessage errorMessage) {
        try {
            remote.sendString(serializer.toJson(errorMessage));
        } catch (IOException e) {
            throw new RuntimeException(e); //this shouldn't happen.
        }
    }

    public Connection getConnection(String authToken, Session session) throws DataAccessException {
        //TODO: need username so I can get the right Connection Object.
        String username = wsVerifyAuthToken(authToken);
        if (username != null) {
            Connection c = new Connection(username, session);
            return c;
        } else {
            throw new UnauthorizedAccessError("Error: unauthorized");
        }

    }

    public String wsVerifyAuthToken(String authToken) throws DataAccessException {
        boolean result = server.getHandler().getAuthService().verifyAuthToken(authToken);
        var authService = server.getHandler().getAuthService();
        if (result) {
            return authService.getAuthData(authToken).username();
        } else {
            throw new UnauthorizedAccessError("Error: unauthorized");
        }
    }

}
