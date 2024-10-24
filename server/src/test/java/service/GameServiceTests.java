package service;

import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    //initialize DAO's
    static final GameService GAME_SERVICE = new GameService();//default is a memory DAO;

    @Test
    void clear() {
        GAME_SERVICE.clearGameDB();
        assertEquals(GAME_SERVICE.sizeof(), 0);
    }


    @Test
    void goodCreateGame() {
        int gameID = GAME_SERVICE.createGame("myGame");
        assertNotEquals(0, gameID);
    }

    @Test
    void badCreateGame() {
        int gameID1 = GAME_SERVICE.createGame("yourGame");
        int gameID2 = GAME_SERVICE.createGame("thisGame");
        assertNotEquals(gameID1, gameID2);
    }

    @Test
    void goodUpdateGame() throws DataAccessException {
        int gameID3 = GAME_SERVICE.createGame("oof");
        GAME_SERVICE.updateGame("white", gameID3, "thisGuy");
        assertEquals("thisGuy", GAME_SERVICE.getGame(gameID3).whiteUsername());
    }

    @Test
    void badUpdateGame() {
        assertThrows(BadServiceRequest.class, () ->
                GAME_SERVICE.updateGame(null, 2, "thisGuy"));
    }

    @Test
    void goodGetGame() {
        int gameID4 = GAME_SERVICE.createGame("oops");
        var result = GAME_SERVICE.getGame(gameID4);
        assertEquals(GameData.class, result.getClass());
    }

    @Test
    void badGetGame() {
        var result = GAME_SERVICE.getGame(0);
        assertNull(result);
    }


}
