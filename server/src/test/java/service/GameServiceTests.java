package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameServiceTests {
    //initialize DAO's
    static final GameService gameService = new GameService();//default is a memory DAO;

    @Test
    void clear() {
        gameService.clearGameDB();
        assertEquals(gameService.sizeof(), 0);
    }

    @Test
    void getGame() {
//        gameService.getGame()
    }


}
