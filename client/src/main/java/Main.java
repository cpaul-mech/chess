import chess.*;
import ui.*;

public class Main {

    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        OverallRepl overallRepl = new OverallRepl(serverUrl);
        overallRepl.run();
    }
}