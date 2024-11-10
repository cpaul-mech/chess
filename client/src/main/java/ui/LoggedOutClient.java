package ui;

import java.util.Arrays;

public class LoggedOutClient {
    private String thisServerURL;

    public LoggedOutClient(String serverURL) {
        thisServerURL = serverURL;
    }

    public String eval(String line) throws BadInputException {
        //need to parse the line
        var tokens = line.toLowerCase().split(" ");
        if (tokens.length == 0) {
            return "";
        } else {
            //cases are help, quit, login, register
            String cmd = tokens[0];
            String[] params = null;
            if (tokens.length > 1) {
                params = Arrays.copyOfRange(tokens, 1, tokens.length);
            }
            return switch (cmd) {
                case "help" -> loggedOutHelp();
                case "quit" -> "quit";
                case "login" -> login(params);
                default -> "cmd: " + cmd + " was not understood. \nProper commands are:\n" + loggedOutHelp();
            };
        }
    }

    public String login(String[] params) throws BadInputException {
        if (params == null || params.length < 3) {
            throw new BadInputException("cmd: login did not have enough parameters.");
        } //TODO: IMPLEMENT HTTP CODE.

        return "";
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
