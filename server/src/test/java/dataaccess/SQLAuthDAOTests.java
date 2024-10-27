package dataaccess;

import org.junit.jupiter.api.Test;


public class SQLAuthDAOTests {

    @Test
    public void configureDBtest() throws DataAccessException {
        try {
            SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException();
        }
    }
}
