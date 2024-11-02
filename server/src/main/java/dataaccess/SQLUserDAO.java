package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDataAccess {

    public SQLUserDAO() throws DataAccessException {
        configureDatabaseUserTable();
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        String createUserString = "INSERT INTO userDB (username, password, email) VALUES" +
                " (?, ?, ?)";
        String[] values = {userData.username(), userData.password(), userData.email()};
        executeOneLineUpdate(createUserString, values);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT password, email FROM userDB WHERE username=?")) {
                preparedStatement.setString(1, username);
                var rs = preparedStatement.executeQuery();
                if (!rs.next()) {
                    return null;
                }
                String password = rs.getString(1);
                String email = rs.getString(2);
                return new UserData(username, password, email);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to execute query" + ex.getMessage());
        }
    }

    @Override
    public void clearUsers() throws DataAccessException {
        String truncateString = "TRUNCATE TABLE userDB";
        executeOneLineUpdate(truncateString, null);
    }

    @Override
    public int dbSize() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM userDB")) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                return rs.getFetchSize();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to execute query" + ex.getMessage());
        }
    }

    private void executeOneLineUpdate(String statement, String[] args) throws DataAccessException {
        SQLAuthDAO.commonOneLineUpdate(statement, args);
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
        SQLAuthDAO.configureDatabaseSpecificTable(createStatements);
    }
}
