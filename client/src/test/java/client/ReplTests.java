package client;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import ui.EscapeSequences;
import ui.OverallRepl;
import ui.ServerFacade;

public class ReplTests {
    private static Server server;
    private static OverallRepl overallRepl;
    private static String serverUrl;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverUrl = "http://localhost:" + port;
        overallRepl = new OverallRepl(serverUrl);
    }

    @Test
    public void runTest() {
        overallRepl.run();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


}
