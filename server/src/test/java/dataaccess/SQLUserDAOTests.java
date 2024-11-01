package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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


}
