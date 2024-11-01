package dataaccess;

import model.GameData;

import java.sql.SQLException;
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
    public Collection<GameData> listGames() {
        return List.of();
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
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
