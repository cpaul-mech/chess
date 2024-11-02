package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDataAccess {
    private final Gson serializer = new Gson();

    public SQLGameDAO() throws DataAccessException {
        configureDatabaseGameTable();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        String createGameString = "INSERT INTO gameDB (gameName, game) VALUES (?, ?)";
        String[] values = {gameName, serializer.toJson(chessGame)};
        executeOneLineUpdate(createGameString, values);
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT gameID FROM gameDB WHERE gameName=?")) {
                preparedStatement.setString(1, gameName);
                var rs = preparedStatement.executeQuery();
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to execute query" + ex.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT whiteUsername, blackUsername," +
                    "gameName, game FROM gameDB WHERE gameID=?")) {
                preparedStatement.setInt(1, gameID);
                var rs = preparedStatement.executeQuery();
                rs.next();
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                ChessGame game = serializer.fromJson(rs.getString("game"), ChessGame.class);
                return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to execute query" + ex.getMessage());
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM gameDB")) {
                List<GameData> gameList = new ArrayList<>();
                var rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    var gameID = rs.getInt("gameID");
                    var password = rs.getString("password");
                    var email = rs.getString("email");
                }
                return List.of();
                //TODO: FINISH AND TEST THIS!!!

            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to execute query" + ex.getMessage());
        }
    }

    @Override
    public void updateGame(int gameIDtoChange, GameData replacementGame) {

    }

    @Override
    public void clearGameDB() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String truncateString = "TRUNCATE TABLE gameDB";
            try (var preparedStatement = conn.prepareStatement(truncateString)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }

    }

    @Override
    public int dbSize() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM gameDB")) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                return rs.getFetchSize();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to execute query" + ex.getMessage());
        }
    }

    private void executeOneLineUpdate(String statement, String[] args) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                if (args != null) {
                    for (int i = 1; i < args.length + 1; i++) {
                        preparedStatement.setString(i, args[i - 1]);
                    }
                }
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to execute statement: " + statement + ", " + ex.getMessage());
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS gameDB (
             gameID INT NOT NULL AUTO_INCREMENT,
             whiteUsername VARCHAR(255),
             blackUsername VARCHAR(255),
             gameName VARCHAR(255) NOT NULL,
             game TEXT NOT NULL,
             PRIMARY KEY (gameID),
             INDEX (gameName)
             ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabaseGameTable() throws DataAccessException {
        SQLAuthDAO.configureDatabaseSpecificTable(createStatements);
    }
}
