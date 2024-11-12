package ui;

import model.UserData;

import java.util.Arrays;

public class LoggedOutClient {
    private String thisServerURL;
    private final ServerFacade server;
    //todo: implement other variables to make sure I store things like the AuthToken and username.
    private String username;

    public LoggedOutClient(String serverURL) {
        thisServerURL = serverURL;
        server = new ServerFacade(thisServerURL);
    }

    public ServerFacade getServer() {
        return server;
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
                case "register" -> register(params);
                default -> "cmd: " + cmd + " was not understood. \nProper commands are:\n" + loggedOutHelp();
            };
        }
    }

    public String login(String[] params) throws BadInputException {
        if (params == null || params.length < 2) {
            throw new BadInputException("cmd: login did not have enough parameters.");
        } //TODO: IMPLEMENT HTTP CODE.
        //Now we have 3 strings, username, password
        UserData loginData = new UserData(params[0], params[1], null); //the login endpoint is expecting a userData
        //object without an email, so serverFacade will be expecting that as well.

        return "You logged in as: " + params[0];
    }

    public String register(String[] params) {
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
