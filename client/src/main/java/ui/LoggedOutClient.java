package ui;

import model.AuthData;
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
                default -> EscapeSequences.SET_TEXT_COLOR_RED + "cmd: '" + cmd + "' was not understood.\n" +
                        EscapeSequences.RESET_TEXT_COLOR + loggedOutHelp();
            };
        }
    }

    public String login(String[] params) throws BadInputException {
        if (params == null || params.length < 2) {
            return "cmd: login did not have enough parameters.\n" +
                    "Please provide your username and password.";
        }
        //Now we have 3 strings, username, password
        UserData loginData = new UserData(params[0], params[1], null); //the login endpoint is expecting a userData
        //object without an email, so serverFacade will be expecting that as well.
        try {
            AuthData returnedAuthData = server.login(loginData);
        } catch (ServerException e) {
            if (e.getrCode() == 500) {
                return "Uh oh, an internal server error occurred: \n" +
                        e.getMessage() +
                        "\nPlease try again!";
            }
            if (e.getrCode() == 401) {
                return String.format(EscapeSequences.SET_TEXT_COLOR_RED + """
                        The username: %s and password: %s that you entered did\s
                        not match our records, please try again or type help to review\s
                        the 'register' command.""", params[0], params[1]);
            }
        }
        return "You logged in as: " + params[0];
    }

    public String register(String[] params) {
        return "";
    }


    public String loggedOutHelp() {
        return EscapeSequences.RESET_TEXT_COLOR +
                """
                        Possible commands are:
                        register <USERNAME> <PASSWORD> <EMAIL> - To create an account.
                        login <USERNAME> <PASSWORD> - To play chess.
                        quit - stop playing chess
                        help - list all possible commands
                        note: bracketed values are parameters.
                        """;
    }
}
