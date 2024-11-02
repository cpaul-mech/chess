package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;


public class SQLAuthDAOTests {
    public SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();

    public SQLAuthDAOTests() throws DataAccessException {
    }

    @Test
    public void configureDBTest() throws DataAccessException {
        try {
            SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void clearAllAuthData() {
        Assertions.assertDoesNotThrow(() -> sqlAuthDAO.clearAuthDB());
    }

    @Test
    public void addNewAuthData() {
        AuthData authData = new AuthData(UUID.randomUUID().toString(), "cpaul");
        Assertions.assertDoesNotThrow(() -> sqlAuthDAO.addAuthData(authData));
    }

//    @Test
//    public void addBadAuthData() {
//        AuthData authData = new AuthData(null, "cpaul2");
//        Assertions.assertThrows(DataAccessException.class, () -> sqlAuthDAO.addAuthData(authData));
//    }

    @Test
    public void getAuthData() throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, "broHomieDudeMan");
        sqlAuthDAO.addAuthData(authData);
        AuthData returnedAuthData = sqlAuthDAO.getAuthData(authToken);
        Assertions.assertEquals(authData, returnedAuthData);
    }
}
