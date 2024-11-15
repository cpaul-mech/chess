package client;

import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import ui.LoggedOutClient;
import ui.OverallRepl;

public class LoggedOutClientTests {
    private static Server server;
    private static OverallRepl overallRepl;
    private static LoggedOutClient loggedOutClient;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String serverUrl = "http://localhost:" + port;
        loggedOutClient = new LoggedOutClient(serverUrl);
    }

    @Test
    public void goodRegisterUser() {
        String[] registerUser = {"HeMan", "sheWoman", "hotmail@gmail.com"};
        var result = loggedOutClient.register(registerUser);
        Assertions.assertNotNull(result);
    }

    @Test
    public void badRegisterUser() {
        String[] registerUser = {"HeMan"};
        String result = loggedOutClient.register(registerUser);
        Assertions.assertFalse(result.contains("You have successfully registered"));
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


}
