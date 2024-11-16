package ui;

import model.AuthData;

import java.util.Arrays;

public class LoggedInClient {
    private AuthData currentAuthToken;
    private String serverUrl;
    private ServerFacade server;

    public LoggedInClient(String url, ServerFacade facade) {
        //when this object is created, it will always be null
        currentAuthToken = null;
        serverUrl = url;
        server = facade;
    }

    public void setCurrentAuthData(AuthData aD) {
        currentAuthToken = aD;
    }

    public String eval(String line) {
        //need to parse the line
        var tokens = line.split(" ");
        if (tokens.length == 0) {
            return "";
        } else {
            //cases are create, list, join, observe, logout, quit, help.
            String cmd = tokens[0].toLowerCase();
            String[] params = null;
            if (tokens.length > 1) {
                params = Arrays.copyOfRange(tokens, 1, tokens.length);
            }
            return switch (cmd) {
                case "help" -> loggedInhelp();
                case "quit" -> quit();
                case "create" -> create(params);
                case "list" -> list(params);
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);
                default -> EscapeSequences.SET_TEXT_COLOR_RED + "cmd: '" + cmd + "' was not understood.\n" +
                        EscapeSequences.RESET_TEXT_COLOR + loggedInhelp();
            };
        }
    }

    public String join(String[] params) {
        return "";
    }

    public String create(String[] params) {
        return "";
    }

    public String quit() {
        //call logout,
        return "quit";
    }

    public String list(String[] params) {
        return "";
    }

    public String logout(String[] params) {
        return "";
    }

    public String observe(String[] params) {
        return "";
    }

    public String loggedInhelp() {
        return """
                options:
                create <Name> - creates a new game called 'NAME'
                list - prints a list of all the games
                join <ID> [WHITE|BLACK] - join a game as either WHITE or BLACK
                observe <ID> - Observe a game as it happens
                logout - log yourself out
                quit - logs you out, and quits chess
                help - print possible commands again""";
    }
}
