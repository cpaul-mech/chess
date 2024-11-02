package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDataAccess {

    public SQLAuthDAO() throws DataAccessException {
        configureDatabaseAuthTable();
    }

    @Override
    public int dbSize() {
        return 0;
    }

    @Override
    public void clearAuthDB() throws DataAccessException {
        String truncateString = "TRUNCATE TABLE authDB";
        executeOneLineUpdate(truncateString, null);
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM authDB WHERE authCode=?")) {
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeQuery();
                rs.next();
                String username = rs.getString(1);
                return new AuthData(authToken, username);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to execute query" + ex.getMessage());
        }
    }

    @Override
    public void addAuthData(AuthData newAuthData) throws DataAccessException {
        //TODO: DO I NEED TO USE THE PREPARESTATEMENTS SYNTAX that we covered in Lecture 18?
        String addAuthString = "INSERT INTO authDB (authCode, username) VALUES (?, ?)";
        String[] values = {newAuthData.authToken(), newAuthData.username()};
        executeOneLineUpdate(addAuthString, values);
    }

    @Override
    public void deleteAuthData(AuthData authData) throws DataAccessException {
        String deleteAuthString = "DELETE FROM authDB WHERE authCode=?";
        String[] values = {authData.authToken()};
        executeOneLineUpdate(deleteAuthString, values);

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
            CREATE TABLE IF NOT EXISTS authDB (
              authCode VARCHAR(255) NOT NULL,
              username VARCHAR(255) NOT NULL,
              PRIMARY KEY (authCode),
              INDEX (username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabaseAuthTable() throws DataAccessException {
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
