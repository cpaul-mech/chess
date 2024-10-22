package service;

import dataaccess.GameDataAccess;
import dataaccess.MemoryGameDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameServiceTests {
    static final GameService service = new GameService(new MemoryGameDAO());

    @Test
    void clear() {
        service.clearGameDB();
    }


}
