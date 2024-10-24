package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
//        GAME_SERVICE.getGame()
    }


}
