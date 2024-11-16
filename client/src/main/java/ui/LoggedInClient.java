package ui;

import model.AuthData;

import java.util.Arrays;

public class LoggedInClient {
    private AuthData currentAuthToken;
    private final ServerFacade server;

    public LoggedInClient(String url, ServerFacade facade) {
        currentAuthToken = null;
        server = facade;
    }

    public void setCurrentAuthData(AuthData aD) {
        currentAuthToken = aD;
    }

    public AuthData getCurrentAuthData() {
        return currentAuthToken;
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
                case "join" -> joinGame(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                default -> EscapeSequences.SET_TEXT_COLOR_RED + "cmd: '" + cmd + "' was not understood.\n" +
                        EscapeSequences.RESET_TEXT_COLOR + loggedInhelp();
            };
        }
    }

    public String joinGame(String[] params) {
        if (params == null || params.length < 2) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "cmd: 'join' did not have enough parameters.\n" +
                    EscapeSequences.RESET_TEXT_COLOR;
        } else {
            try {
                JoinGameInput joinGameInput = new JoinGameInput(params[1], Integer.parseInt(params[0]));
                server.joinGame(currentAuthToken, joinGameInput); //do I need to do anything with this?
            } catch (ServerException e) {
                if (e.getrCode() == 500) {
                    return EscapeSequences.SET_TEXT_COLOR_RED + "Uh oh, an internal server error occurred: \n" +
                            e.getMessage() +
                            "\nPlease try again!" + EscapeSequences.RESET_TEXT_COLOR;
                }
                if (e.getrCode() == 401) {
                    return EscapeSequences.SET_TEXT_COLOR_RED + """
                            something strange happened, please re-login!""" + EscapeSequences.RESET_TEXT_COLOR;
                }
                if (e.getrCode() == 403) {
                    return EscapeSequences.SET_TEXT_COLOR_RED + "The player color you entered was not 'WHITE' or 'BLACK'" +
                            "\n*not case sensitive*\n" +
                            "Please try again." + EscapeSequences.RESET_TEXT_COLOR;
                }
            } catch (NumberFormatException e) {
                return EscapeSequences.SET_TEXT_COLOR_RED + """
                        The game number you provided was invalid
                        please try again.""" + EscapeSequences.RESET_TEXT_COLOR;
            }
            //join game successful
            return EscapeSequences.SET_TEXT_COLOR_GREEN + "You have successfully joined the game as player: " + params[1]
                    + EscapeSequences.RESET_TEXT_COLOR;
        }
    }


    public String create(String[] params) {
        if (params == null || params.length < 1) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "cmd: 'create' did not have enough parameters.\n" +
                    EscapeSequences.RESET_TEXT_COLOR;
        } else {
            try {
                int newGameID = server.createGame(currentAuthToken, params[0]);
            } catch (ServerException e) {
                if (e.getrCode() == 500) {
                    return EscapeSequences.SET_TEXT_COLOR_RED + "Uh oh, an internal server error occurred: \n" +
                            e.getMessage() +
                            "\nPlease try again!" + EscapeSequences.RESET_TEXT_COLOR;
                }
                if (e.getrCode() == 401) {
                    return EscapeSequences.SET_TEXT_COLOR_RED + """
                            something strange happened, please re-login!""" + EscapeSequences.RESET_TEXT_COLOR;
                }
                if (e.getrCode() == 400) {
                    return EscapeSequences.SET_TEXT_COLOR_RED + """
                            Provided gameName was rejected?""" + EscapeSequences.RESET_TEXT_COLOR;
                }
            }
            return EscapeSequences.SET_TEXT_COLOR_GREEN + String.format("Created game '%s'", params[0]) +
                    "\nPlease run 'list' to list this and other games you can join.";
        }
    }

    public String quit() {
        var logoutLine = logout();
        System.out.println(logoutLine);
        return "quit";
    }

    public String list(String[] params) {
        return "";
    }

    public String logout() {
        String username = currentAuthToken.username();
        currentAuthToken = null; //reset the authData so that I can read that in the repl.
        return String.format("Logging '%s' out.", username) +
                "\nrerun 'help' for commands again if needed.";
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
