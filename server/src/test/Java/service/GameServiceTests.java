package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameServiceTests {
    //initalize DAO's
    static final GameService _game_service = new GameService();//defualt is a memory DAO;

    @Test
    void clear() {
        _game_service.clearGameDB();
        assertEquals(_game_service.sizeof(), 0);
    }


}
