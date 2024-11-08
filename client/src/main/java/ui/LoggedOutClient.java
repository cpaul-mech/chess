package ui;

public class LoggedOutClient {
    public LoggedOutClient() {

    }

    public String eval(String line) {
        //need to parse the line
        var tokens = line.toLowerCase().split(" ");
        if (tokens.length == 0) {
            return "";
        } else {
            //cases are help, quit, login, register
            String cmd = tokens[0];
            return switch (cmd) {
                case "help" -> loggedOutHelp();
                case "quit" -> "quit";
                default -> "input: " + line + " was not understood. \nProper commands are:\n" + loggedOutHelp();
            };
        }
    }

    public String loggedOutHelp() {
        return """
                Possible commands are:
                register <USERNAME> <PASSWORD> <EMAIL> - To create an account.
                login <USERNAME> <PASSWORD> - To play chess.
                quit - stop playing chess
                help - list all possible commands
                note: bracketed values are parameters.
                """;
    }
}
