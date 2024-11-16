package client;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.JoinGameInput;
import ui.ServerException;
import ui.ServerFacade;

import java.util.Collection;
import java.util.ServiceConfigurationError;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearServer() throws ServerException {
        Assertions.assertDoesNotThrow(() -> serverFacade.clearDataBases());
    }

    @Test
    public void successfulLogoutTest() throws ServerException {
        UserData registerData = new UserData("cpauldude", "Perspiration", "8ball@gmail.com");
        AuthData authData = serverFacade.registerUser(registerData);
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(authData));
    }

    @Test
    public void badLogoutTest() {
        AuthData badAuthData = new AuthData("thisissupersecure!", "cpaul12345");
        Assertions.assertThrows(ServerException.class, () -> serverFacade.logout(badAuthData));
    }

    @Test
    public void loginTest() throws ServerException {
        UserData registerData = new UserData("cpauldude", "Perspiration", "8ball@gmail.com");
        AuthData authData = serverFacade.registerUser(registerData);
        serverFacade.logout(authData);
        // now the authtoken isn't relevant, but the username is still registered.
        Assertions.assertDoesNotThrow(() ->
                serverFacade.login(new UserData("cpauldude", "Perspiration", null)));
    }

    @Test
    public void badLogInTest() {
        Assertions.assertThrows(ServerException.class,
                () -> serverFacade.login(new UserData("notUser", "notPass", null)));
    }


    @Test
    public void successfulRegisterTest() throws ServerException {
        UserData registerData = new UserData("cpaul", "PERSEVERANCE", "8ball@gmail.com");
        Assertions.assertDoesNotThrow(() -> serverFacade.registerUser(registerData));
    }

    @Test
    public void badRegisterTest() {
        Assertions.assertThrows(ServerException.class, () -> serverFacade.registerUser(
                new UserData("myNameIsMe", null, "dudeCrush@byu.com")));
    }

    @Test
    public void createGameTest() throws ServerException {
        UserData registerData = new UserData("cpaul", "PERSEVERANCE", "8ball@gmail.com");
        var authData = serverFacade.registerUser(registerData);
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame(authData, "myNewGame"));
    }

    @Test
    public void badCreateGameTest() throws ServerException {
        UserData registerData = new UserData("cpaul2", "PERSEVERANCE2", "8ball2@gmail.com");
        var authData = serverFacade.registerUser(registerData);
        Assertions.assertThrows(ServerException.class, () -> serverFacade.createGame(new AuthData("notathing",
                "sbgidiahd"), "newGame"));
    }

    @Test
    public void goodJoinGameTest() throws ServerException {
        UserData registerData = new UserData("cpaul3", "PERSEVERANCE3", "8ball3@gmail.com");
        var authData = serverFacade.registerUser(registerData);
        var gameID = serverFacade.createGame(authData, "myNewGame");
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(authData, new JoinGameInput("WHITE", gameID)));
    }

    @Test
    public void badJoinGameTest() throws ServerException {
        UserData registerData = new UserData("cpaul3", "PERSEVERANCE3", "8ball3@gmail.com");
        var authData = serverFacade.registerUser(registerData);
        Assertions.assertThrows(ServerException.class, () -> serverFacade.joinGame(authData, new JoinGameInput("notacolor", 123)));
    }

    @Test
    public void listGames() throws ServerException {
        UserData registerData = new UserData("cpaul3", "PERSEVERANCE3", "8ball3@gmail.com");
        var authData = serverFacade.registerUser(registerData);
        serverFacade.createGame(authData, "game1");
        serverFacade.createGame(authData, "game2");
        serverFacade.createGame(authData, "game3");
        Assertions.assertDoesNotThrow(() -> serverFacade.listGames(authData));
        Collection<GameData> gamesList = serverFacade.listGames(authData);
    }

}
