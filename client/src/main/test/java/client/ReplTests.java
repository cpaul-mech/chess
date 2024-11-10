package client;

import org.junit.jupiter.api.Test;
import ui.EscapeSequences;
import ui.OverallRepl;

public class ReplTests {
    String server = "http://localhost:8080";
    OverallRepl overallRepl = new OverallRepl(server);

    @Test
    public void runTest() {
        overallRepl.run();
    }
}
