package dataaccess;

import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.GameService;

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

    public void addNewGame() {
//        GameData gameData = new GameData(1,

    }

    @Test
    public void clearAllGames() throws DataAccessException {

    }

}
