package client;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerException;
import ui.ServerFacade;

import java.util.ServiceConfigurationError;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        //now I'll need to declare a new serverfacade class and stuff.
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void clearServer() throws ServerException {
        serverFacade.clearDataBases();
    }

    @Test
    public void successfulLogoutTest() {

    }


    @Test
    public void successfulRegisterTest() throws ServerException {
        UserData registerData = new UserData("cpaul", "PERSEVERANCE", "8ball@gmail.com");
        Assertions.assertDoesNotThrow(() -> serverFacade.registerUser(registerData));
    }

}
