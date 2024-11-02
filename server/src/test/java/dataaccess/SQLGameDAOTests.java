package dataaccess;

import chess.ChessGame;
import model.GameData;
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

    @Test
    public void getGame() throws DataAccessException {
        String gameName = "helpThisGame";
        int gameID = sqlGameDAO.createGame(gameName);
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        GameData gameData2 = sqlGameDAO.getGame(gameID);
        Assertions.assertEquals(gameData, gameData2);
    }

    @Test
    public void updateGame() throws DataAccessException {
        int gameID = sqlGameDAO.createGame("theWorstGame2");
        sqlGameDAO.createGame("myGame");
        String newWhiteUsername = "dwayneTheRockJohnson";
        GameData updatedGame = new GameData(gameID, newWhiteUsername, null, "theWorstGame", new ChessGame());
        sqlGameDAO.updateGame(gameID, updatedGame);
        Assertions.assertEquals(updatedGame, sqlGameDAO.getGame(gameID));
    }
}
