package dataaccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTests {
    SQLGameDAO sqlGameDAO = new SQLGameDAO();

    public SQLGameDAOTests() throws DataAccessException {
    }

    @Test
    public void configureDB() {
        //TODO: make this test more intelligent.
        try {
            SQLGameDAO sqlGameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void addNewGame() throws DataAccessException {
        clearAllGames();
        int gameID = sqlGameDAO.createGame("theBestGame");
        assertEquals(1, gameID);
    }

    @Test
    public void clearAllGames() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> sqlGameDAO.clearGameDB());
    }


}
