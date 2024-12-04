package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import commands.ConnectCommand;
import commands.UserGameCommand;
import dataaccess.DataAccessException;
import messages.ErrorMessage;
import messages.LoadGameMessage;
import messages.NotificationMessage;
import model.GameData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;

import java.io.IOException;
import java.util.Objects;

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
            Connection conn = getConnection(command.getAuthToken(), session); //this verifies authToken!!
            if (conn != null) {
                switch (command.getCommandType()) {
                    case CONNECT -> connect(conn, message);
                    case LEAVE -> leave(conn, message);
                    case MAKE_MOVE -> makeMove(conn, message);
                    case RESIGN -> resign(conn, message);
                    case null, default -> {
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            sendErrorMessage(session.getRemote(), new ErrorMessage(ex.getMessage()));
        }

    }

    private void resign(Connection conn, String message) {

    }

    private void makeMove(Connection conn, String message) {
    }

    private void leave(Connection conn, String message) {
    }

    private void connect(Connection conn, String msg) {

        try {
            connections.add(conn);
            ConnectCommand connectCommand = serializer.fromJson(msg, ConnectCommand.class);
            String color = getPlayerColor(conn, connectCommand);
            String gameName = getGameData(connectCommand).gameName();
            String connectNotification = String.format("Player: %s, joined game %s as %s", conn.userName, gameName, color);
            NotificationMessage noti = new NotificationMessage(connectNotification);
            connections.broadcast(conn.userName, noti);

            LoadGameMessage lgm = createLoadGameMessage(connectCommand);
            conn.session.getRemote().sendString(serializer.toJson(lgm));

        } catch (IOException e) {
            sendErrorMessage(conn.session.getRemote(), new ErrorMessage("an IOException occurred..."));
        } catch (DataAccessException e) {
            sendErrorMessage(conn.session.getRemote(), new ErrorMessage("Error: unauthorized"));
        }

    }

    private String getPlayerColor(Connection conn, ConnectCommand connectCommand) throws DataAccessException {
        String username = conn.userName;
        GameData gameData = getGameData(connectCommand);
        //check if it matches either player color
        if (Objects.equals(gameData.blackUsername(), username)) {
            return "BLACK";
        } else if (Objects.equals(gameData.whiteUsername(), username)) {
            return "WHITE";
        } else {
            return null;
        }
    }

    private GameData getGameData(ConnectCommand connectCommand) throws DataAccessException {
        return server.getHandler().getGameService().getGame(connectCommand.getGameID());
    }

    private LoadGameMessage createLoadGameMessage(ConnectCommand con) {
        try {
            ChessGame game = server.getHandler().getGameService().getGame(con.getGameID()).game();
            return new LoadGameMessage(game);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public void sendErrorMessage(RemoteEndpoint remote, ErrorMessage errorMessage) {
        try {
            remote.sendString(serializer.toJson(errorMessage));
        } catch (IOException e) {
            throw new RuntimeException(e); //this shouldn't happen.
        }
    }

    public Connection getConnection(String authToken, Session session) {
        try {
            String username = wsVerifyAuthToken(authToken);
            if (username != null) {
                return new Connection(username, session);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            return null;
        }
    }

    public String wsVerifyAuthToken(String authToken) throws DataAccessException {
        boolean result = server.getHandler().getAuthService().verifyAuthToken(authToken);
        var authService = server.getHandler().getAuthService();
        if (result) {
            return authService.getAuthData(authToken).username();
        } else {
            return null;
        }
    }

}
