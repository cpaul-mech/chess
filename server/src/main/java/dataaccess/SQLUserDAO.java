package dataaccess;

import model.UserData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class SQLUserDAO implements UserDataAccess {

    public SQLUserDAO() throws DataAccessException {
        configureDatabaseUserTable();
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        String createUserString = String.format("INSERT INTO userDB (username, password, email) VALUES" +
                " ('%s', '%s', '%s')", userData.username(), userData.password(), userData.email());
        executeOneLineUpdate(createUserString);
    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void clearUsers() throws DataAccessException {
        String truncateString = "TRUNCATE TABLE userDB";
        executeOneLineUpdate(truncateString);
    }

    @Override
    public int dbSize() {
        return 0;
    }

    @Override
    public Collection<UserData> listUsers() {
        return List.of();
    }

    private void executeOneLineUpdate(String statement) throws DataAccessException {
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
            CREATE TABLE IF NOT EXISTS userDB (
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabaseUserTable() throws DataAccessException {
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
