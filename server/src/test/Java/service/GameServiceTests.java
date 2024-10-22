package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Test;

public class GameServiceTests {
    //initalize DAO's
    static final Service service = new Service(new MemoryGameDAO(), new MemoryUserDAO(), new MemoryAuthDAO());

    @Test
    void clear() {
        service.clearGameDB();
    }


}
