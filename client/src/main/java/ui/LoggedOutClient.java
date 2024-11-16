package ui;

import model.AuthData;
import model.UserData;

import java.util.Arrays;

public class LoggedOutClient {
    private String thisServerURL;
    private final ServerFacade server;
    //todo: implement other variables to make sure I store things like the AuthToken and username.
    private AuthData currentUserAuthData;

    public LoggedOutClient(String serverURL) {
        thisServerURL = serverURL;
        server = new ServerFacade(thisServerURL);
        currentUserAuthData = null;
    }

    public ServerFacade getServer() {
        return server;
    }

    public String eval(String line) throws BadInputException {
        //need to parse the line
        var tokens = line.split(" ");
        if (tokens.length == 0) {
            return "";
        } else {
            //cases are help, quit, login, register
            String cmd = tokens[0].toLowerCase();
            String[] params = null;
            if (tokens.length > 1) {
                params = Arrays.copyOfRange(tokens, 1, tokens.length);
            }
            return switch (cmd) {
                case "help" -> loggedOutHelp();
                case "quit" -> quit();
                case "login" -> login(params);
                case "register" -> register(params);
                default -> EscapeSequences.SET_TEXT_COLOR_RED + "cmd: '" + cmd + "' was not understood.\n" +
                        EscapeSequences.RESET_TEXT_COLOR + loggedOutHelp();
            };
        }
    }

    public String login(String[] params) throws BadInputException {
        if (params == null || params.length < 2) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "cmd: login did not have enough parameters.\n" +
                    "Please provide your username and password." + EscapeSequences.RESET_TEXT_COLOR;
        }
        UserData loginData = new UserData(params[0], params[1], null);
        try {
            currentUserAuthData = server.login(loginData);

            //this means that the login was successful!!
            //need to store this authData somehow
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
        return EscapeSequences.SET_TEXT_COLOR_GREEN + "You logged in as: " + params[0] +
                EscapeSequences.RESET_TEXT_COLOR + loginStatements();
    }

    public String register(String[] params) {
        if (params == null || params.length < 3) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "cmd: register did not have enough parameters.\n" +
                    "Please provide your username, password, and email." + EscapeSequences.RESET_TEXT_COLOR;
        }
        UserData registerData = new UserData(params[0], params[1], params[2]);
        try {
            currentUserAuthData = server.registerUser(registerData);
        } catch (ServerException e) {
            if (e.getrCode() == 500) {
                return "Uh oh, an internal server error occurred: \n" +
                        e.getMessage() +
                        "\nPlease try again!";
            }
            if (e.getrCode() == 403) {
                return String.format(EscapeSequences.SET_TEXT_COLOR_RED + """
                        The username: %s that you entered is already registered\s
                        please use the 'login' command or type help to review\s
                        other commands.""", params[0]) + EscapeSequences.RESET_TEXT_COLOR;
            }
        }
        return EscapeSequences.SET_TEXT_COLOR_GREEN + String.format("You have successfully registered as '%s'",
                params[0]) + EscapeSequences.RESET_TEXT_COLOR + loginStatements();

    }

    public String loginStatements() {
        return """
                \nYou are now logged in, please enter 'help'
                to review the updated
                list of possible commands.""";
    }

    public String quit() {
        //this function will make sure to delete the current active user's authData if they haven't logged out.
        return "quit";
    }

    public AuthData getCurrentUserAuthData() {
        return currentUserAuthData;
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
