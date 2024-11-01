package dataaccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


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
}
