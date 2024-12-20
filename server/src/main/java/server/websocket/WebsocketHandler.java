package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import websocket.commands.*;
import dataaccess.DataAccessException;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
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
            Connection conn = createIncompleteConnection(command.getAuthToken(), session); //this verifies authToken!!
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
        try {
            ResignCommand resignCommand = serializer.fromJson(message, ResignCommand.class);
            conn = connections.getConnection(conn.userName);
            //need to send a notificaiton saying that a player resigned the game. the game is now over, and cannot be played anymore
            //but doesn't actually force them to leave the game!!
            GameData gameData = getGameData(resignCommand.getGameID());
            if (gameData.game().isTheGameOver()) {
                throw new Exception("The game is over, no other players can resign.");
            } else if (conn.role == Connection.Role.OBSERVER) {
                throw new Exception("You are an observer and cannot resign for an actual player.");
            }
            //now that we have access to the chessGame class, we can set the game to over
            gameData.game().setGameOver();
            GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                    gameData.gameName(), gameData.game());
            server.getHandler().updateEntireGame(resignCommand.getAuthToken(), newGameData);
            String resignMessage = String.format("Player: '%s' has forfeited the game.", conn.userName);
            NotificationMessage notificationMessage = new NotificationMessage(resignMessage);
            connections.broadcastToAllInGame(resignCommand.getGameID(), notificationMessage);
            //now overwrite it, it won't ever return any valid moves!!
        } catch (Exception e) {
            sendErrorMessage(conn.session.getRemote(), new ErrorMessage(e.getMessage()));
        }
    }

    private void makeMove(Connection conn, String message) {
        try {
            MakeMoveCommand moveCommand = serializer.fromJson(message, MakeMoveCommand.class);
            conn = connections.getConnection(conn.userName); //update the connection field
            //the chessGame object has the error checking functionality for moving, and then the game must be stored anew through the updateGame
            //method accessed through the handler object that I just wrote.
            GameData gameData = getGameData(moveCommand.getGameID());
            //check for if Move is valid
            ChessGame chessGame = gameData.game();
            ChessMove move = moveCommand.move;
            //need to create a chessGame method that will check which color piece this does.
            if (!Objects.equals(conn.role.toString(), chessGame.getTeamTurn().toString()) || conn.role == Connection.Role.OBSERVER) {
                throw new Exception("Player is trying to move another players piece.");
            }
            chessGame.makeMove(move); //this will throw some kind of exception if the move is incorrect.
            LoadGameMessage lgm = createLoadGameMessage(moveCommand);
            String moveNotification = String.format("Player: '%s' made move: '%s'.", conn.userName, move);
            NotificationMessage notificationMessage = new NotificationMessage(moveNotification);
            connections.broadcastToAllInGame(gameData.gameID(), conn.userName, notificationMessage);
            //now check for if checkmate has occured.
            //need to add notification that the other team is in check!!
            ChessGame.TeamColor checkColorForCheck = null;
            String otherPlayerName = "";
            switch (conn.role) {
                case WHITE -> {
                    checkColorForCheck = ChessGame.TeamColor.BLACK;
                    otherPlayerName = gameData.blackUsername();
                }
                case BLACK -> {
                    checkColorForCheck = ChessGame.TeamColor.WHITE;
                    otherPlayerName = gameData.whiteUsername();
                }
            }
            if (chessGame.isInCheckmate(checkColorForCheck)) {
                String winMessage = moveNotification + "\n" + String.format("Player: '%s' on team '%s' is in check " +
                                "mate!\n" +
                                "Congratulations Player: '%s', you have won the game!", otherPlayerName,
                        checkColorForCheck.toString(), conn.userName);
                NotificationMessage winNotification = new NotificationMessage(winMessage);
                connections.broadcastToAllInGame(moveCommand.getGameID(), winNotification);
                chessGame.setGameOver();
            } else if (chessGame.isInCheck(checkColorForCheck)) {
                String checkMessage = String.format("Player: '%s' on team '%s' is in check!", otherPlayerName,
                        checkColorForCheck.toString());
                NotificationMessage checkNotification = new NotificationMessage(checkMessage);
                connections.broadcastToAllInGame(moveCommand.getGameID(), checkNotification);
            }
            GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                    gameData.gameName(), chessGame);
            //now store the newest chessgame, then send out a notification.
            server.getHandler().updateEntireGame(moveCommand.getAuthToken(), newGameData);
            //now broadcast the LoadGameMessage to all players!!
            connections.broadcastToAllInGame(gameData.gameID(), lgm);

        } catch (Exception e) {
            sendErrorMessage(conn.session.getRemote(), new ErrorMessage(e.getMessage()));
        }
    }

    private void leave(Connection conn, String message) {
        try {
            LeaveCommand leaveCommand = serializer.fromJson(message, LeaveCommand.class);
            //need to remove the connection object from the serializer. but first make sure that we notify everyone else properly.
            String color = getPlayerColor(conn, leaveCommand);
//            updateConnectionFields(conn, color, leaveCommand.getGameID());
            //or we could do this.
            Connection actConn = connections.getConnection(conn.userName);
            conn = actConn;
            String gameName = getGameData(leaveCommand.getGameID()).gameName();
            String leavingNotification = String.format("Player: '%s', role: '%s', has left game '%s'."
                    , conn.userName, conn.role, gameName);
            NotificationMessage n = new NotificationMessage(leavingNotification);
            connections.broadcastToAllInGame(leaveCommand.getGameID(), conn.userName, n);
            connections.remove(conn.userName);
            //need to remove that username from the game.
            GameData gameData = getGameData(leaveCommand.getGameID());
            if (conn.role != Connection.Role.OBSERVER) {
                switch (conn.role) {
                    case BLACK -> gameData = new GameData(gameData.gameID(), gameData.whiteUsername(), null,
                            gameData.gameName(), gameData.game());
                    case WHITE -> gameData = new GameData(gameData.gameID(), null, gameData.blackUsername(),
                            gameData.gameName(), gameData.game());
                }
                server.getHandler().updateEntireGame(leaveCommand.getAuthToken(), gameData);
            }

        } catch (Exception e) {
            sendErrorMessage(conn.session.getRemote(), new ErrorMessage(e.getMessage()));
        }
    }

    private void updateConnectionFields(Connection conn, String color, Integer gameID) throws DataAccessException {
        switch (color) {
            case "BLACK" -> conn.setRole(Connection.Role.BLACK);
            case "WHITE" -> conn.setRole(Connection.Role.WHITE);
            case null -> conn.setRole(Connection.Role.OBSERVER);
            default -> throw new DataAccessException("GameID Not found!");
        }
        conn.setGameID(gameID);
    }

    private void connect(Connection conn, String msg) {
        try {
            ConnectCommand connectCommand = serializer.fromJson(msg, ConnectCommand.class);
            //now we have the connectCommand, giving us the gameID of the intended game, and thus which color the player belongs to.
            String color = getPlayerColor(conn, connectCommand);
            updateConnectionFields(conn, color, connectCommand.getGameID());
            connections.add(conn); //now it's safe to add the connection!!
            String gameName = getGameData(connectCommand.getGameID()).gameName();
            String connectNotification = String.format("Player '%s', joined game '%s' as '%s'", conn.userName, gameName,
                    conn.role.toString());
            NotificationMessage note = new NotificationMessage(connectNotification);
            connections.broadcastToAllInGame(conn.gameID, conn.userName, note);

            LoadGameMessage lgm = createLoadGameMessage(connectCommand);
            conn.session.getRemote().sendString(serializer.toJson(lgm));

        } catch (IOException e) {
            sendErrorMessage(conn.session.getRemote(), new ErrorMessage("an IOException occurred..."));
        } catch (DataAccessException e) {
            sendErrorMessage(conn.session.getRemote(), new ErrorMessage(e.getMessage()));
        }

    }

    private String getPlayerColor(Connection conn, UserGameCommand command) throws DataAccessException {
        String username = conn.userName;
        GameData gameData = getGameData(command.getGameID());
        //check if it matches either player color
        if (gameData == null) {
            throw new DataAccessException("GameID not found!!");
        } else if (Objects.equals(gameData.blackUsername(), username)) {
            return "BLACK";
        } else if (Objects.equals(gameData.whiteUsername(), username)) {
            return "WHITE";
        } else {
            return null;
        }
    }

    private GameData getGameData(Integer gameID) throws DataAccessException {
        return server.getHandler().getGameService().getGame(gameID);
    }

    private LoadGameMessage createLoadGameMessage(UserGameCommand command) {
        try {
            ChessGame game = getGameData(command.getGameID()).game();
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

    public Connection createIncompleteConnection(String authToken, Session session) throws DataAccessException {
        String username = wsVerifyAuthToken(authToken);
        if (username != null) {
            return new Connection(username, session, null, -1);
        } else {
            return null;
        }

    }

    public String wsVerifyAuthToken(String authToken) throws DataAccessException {
        boolean result = server.getHandler().getAuthService().verifyAuthToken(authToken);
        var authService = server.getHandler().getAuthService();
        if (result) {
            return authService.getAuthData(authToken).username();
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

}
