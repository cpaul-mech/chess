package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GameServiceTests {
    //initialize DAO's
    static final GameService GAME_SERVICE = new GameService();//default is a memory DAO;

    @Test
    void clear() {
        GAME_SERVICE.clearGameDB();
        assertEquals(GAME_SERVICE.sizeof(), 0);
    }

    @Test
    void getGame() {
//
    }

    @Test
    void createGame() {
        int gameID = GAME_SERVICE.createGame("myGame");
        assertNotEquals(0, gameID);
    }

    @Test
    void badCreateGame() {
        int gameID1 = GAME_SERVICE.createGame("yourGame");
        int gameID2 = GAME_SERVICE.createGame("thisGame");
        assertNotEquals(gameID1, gameID2);
    }


}
