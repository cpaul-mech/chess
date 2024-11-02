package dataaccess;

import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class SQLUserDAOTests {
    SQLUserDAO sqlUserDAO = new SQLUserDAO();

    public SQLUserDAOTests() throws DataAccessException {
    }

    @Test
    public void configureDAOtest() throws DataAccessException {
        var userDAO = new SQLUserDAO();
    }

    @Test
    public void clearAllUsers() {
        Assertions.assertDoesNotThrow(() -> sqlUserDAO.clearUsers());
    }

    @Test
    public void createUser() {
        UserData newUserData = new UserData("cpaul", "mynameisJEFF", "myname@gmail.comm");
        Assertions.assertDoesNotThrow(() -> sqlUserDAO.createUser(newUserData));
    }

    @Test
    public void getUserData() throws DataAccessException {
        UserData newUserData = new UserData("cpaul15", "mynameisJEFFHAW", "myname@gmail.comm");
        sqlUserDAO.createUser(newUserData);
        UserData returnedUserData = sqlUserDAO.getUser("cpaul15");
        Assertions.assertEquals(newUserData, returnedUserData);
    }

}
