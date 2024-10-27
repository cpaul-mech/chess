package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDataAccess {

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public int dbSize() {
        return 0;
    }

    @Override
    public void clearAuthDB() {

    }

    @Override
    public AuthData getAuthData(String authToken) {
        return null;
    }

    @Override
    public void addAuthData(AuthData newAuthData) {

    }

    @Override
    public void deleteAuthData(AuthData authData) {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS userDB (
             username VARCHAR(255) NOT NULL,
             password VARCHAR(255) NOT NULL,
             email VARCHAR(255) NOT NULL,
             PRIMARY KEY (username)
             ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabase() throws DataAccessException {
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
