package client;

import org.junit.jupiter.api.Test;
import ui.EscapeSequences;
import ui.OverallRepl;

public class ReplTests {
    OverallRepl overallRepl = new OverallRepl();

    @Test
    public void runTest() {
        overallRepl.run();
    }
}
