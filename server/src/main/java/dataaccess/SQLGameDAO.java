package dataaccess;

import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDataAccess {

    public SQLGameDAO() throws DataAccessException {
        configureDatabaseGameTable();
    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
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
    public int dbSize() {
        return 0;
    }

    private void executeOneLineStatement(String statement) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to execute statement: " + statement + ", " + ex.getMessage());
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS gameDB (
             gameID INT NOT NULL AUTO_INCREMENT, whiteUsername VARCHAR(255), blackUsername VARCHAR(255),
             gameName VARCHAR(255) NOT NULL,
             game TEXT NOT NULL,
             PRIMARY KEY (gameID)
             ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabaseGameTable() throws DataAccessException {
        SQLAuthDAO.configureDatabaseSpecificTable(createStatements);
    }
}
