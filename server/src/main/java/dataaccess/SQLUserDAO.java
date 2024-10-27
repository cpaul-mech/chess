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
    public void createUser(UserData userData) {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void clearUsers() {

    }

    @Override
    public int dbSize() {
        return 0;
    }

    @Override
    public Collection<UserData> listUsers() {
        return List.of();
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
