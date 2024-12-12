package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import server.websocket.Connection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoggedInClient {
    private AuthData currentAuthData;
    private final ServerFacade server;
    private final Map<Integer, GameData> gamesMap = new HashMap<>();
    public String playerColor;
    public Integer gameID;
    public GameData gameData;
    //maybe I could have the loggedinClient be intelligent.

    public LoggedInClient(String url, ServerFacade facade) {
        currentAuthData = null;
        server = facade;
        gameID = null;
        playerColor = null;
    }

    public void setCurrentAuthData(AuthData aD) {
        currentAuthData = aD;
    }

    public AuthData getCurrentAuthData() {
        return currentAuthData;
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
                case "help" -> loggedInHelp();
                case "quit" -> quit();
                case "create" -> create(params);
                case "list" -> list();
                case "play" -> joinGame(params);
                case "observe" -> observe(params);
                case "logout" -> logout();

                default -> EscapeSequences.SET_TEXT_COLOR_RED + "cmd: '" + cmd + "' was not understood.\n" +
                        EscapeSequences.RESET_TEXT_COLOR + loggedInHelp();
            };
        }
    }


    public String joinGame(String[] params) {
        if (params == null || params.length < 2) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "cmd: 'join' did not have enough parameters.\n" +
                    EscapeSequences.RESET_TEXT_COLOR;
        }
        if (gamesMap.isEmpty()) {
            return "There are no games to join.\nPlease type 'help' to review syntax for creating a new game.";
        } else {
            try {
                //need to get correct gameID:
                GameData gameToJoin = gamesMap.get(Integer.parseInt(params[0]));
                //need to check for if there is no game.
                if (gameToJoin == null) {
                    return EscapeSequences.SET_TEXT_COLOR_RED + "game number: " + Integer.parseInt(params[0]) + " does not exist." +
                            "\nPlease type 'list' to list games again and run join with a valid game number.";
                }
                JoinGameInput joinGameInput = new JoinGameInput(params[1], gameToJoin.gameID());
                server.joinGame(currentAuthData, joinGameInput); //do I need to do anything with this?
                playerColor = params[1];
                gameID = gameToJoin.gameID();
                gameData = gameToJoin;
                //need to store the

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
                    return EscapeSequences.SET_TEXT_COLOR_RED + "The player color you entered was not 'WHITE' or 'BLACK'" +
                            "\n*not case sensitive*\n" +
                            "Please try again." + EscapeSequences.RESET_TEXT_COLOR;
                }
                if (e.getrCode() == 403) {
                    return EscapeSequences.SET_TEXT_COLOR_RED + "The game you chose already has someone playing as " +
                            params[1].toUpperCase() + EscapeSequences.RESET_TEXT_COLOR;
                }
            } catch (NumberFormatException e) {
                return EscapeSequences.SET_TEXT_COLOR_RED + """
                        The game number you provided was invalid
                        please try again.""" + EscapeSequences.RESET_TEXT_COLOR;
            }
            //join game successful
            return EscapeSequences.SET_TEXT_COLOR_GREEN + "You have successfully joined the game " + params[0] + " as player: " +
                    currentAuthData.username() + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }
    }

    public String create(String[] params) {
        if (params == null || params.length < 1) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "cmd: 'create' did not have enough parameters.\n" +
                    EscapeSequences.RESET_TEXT_COLOR;
        } else {
            try {
                int newGameID = server.createGame(currentAuthData, params[0]);
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
        playerColor = null;
        gameID = null;
        System.out.println(logoutLine);
        return "quit";
    }

    public String list() {
        List<GameData> gamesList = null;
        try {
            gamesList = (List<GameData>) server.listGames(currentAuthData);
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
        }
        //iterate through the gamesList and print the ID number, gameName, and player names.
        if (gamesList == null || gamesList.isEmpty()) {
            return "No games found, please type 'help' to review the syntax for creating a game.";
        }
        mapGamesList(gamesList);
        StringBuilder gameListString = new StringBuilder();
        gameListString.append("""
                Printing List of Current Games:
                GameNumber, Game Name, White Player Name, Black Player Name
                """);
        for (int i = 1; i < gamesMap.size() + 1; i++) {
            GameData gameData = gamesMap.get(i);
            String gameName = gameData.gameName();
            String whitePlayer = gameData.whiteUsername();
            if (whitePlayer == null) {
                whitePlayer = "none";
            }
            String blackPlayer = gameData.blackUsername();
            if (blackPlayer == null) {
                blackPlayer = "none";
            }
            gameListString.append(String.format("%d, %s, %s, %s\n", i, gameName, whitePlayer, blackPlayer));
        }
        return gameListString + "To join or observe a game, use the gameNumber to specify the game you want." +
                "\ntype 'help' to review syntax if necessary.";

    }

    public void mapGamesList(List<GameData> gamesList) {
        //this is where I will store the games.
        for (int i = 1; i < gamesList.size() + 1; i++) {
            //the index will serve as the game's number.
            gamesMap.put(i, gamesList.get(i - 1));
        }
    }

    public String logout() {
        String username = currentAuthData.username();
        currentAuthData = null; //reset the authData so that I can read that in the repl.
        return String.format("Logging '%s' out.", username) +
                "\nrerun 'help' for commands again if needed.";
    }

    public String observe(String[] params) {
        if (params == null || params.length < 1) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "cmd: 'observe' did not have enough parameters.\n" +
                    EscapeSequences.RESET_TEXT_COLOR;
        }
        if (gamesMap.isEmpty()) {
            return "There are no games to join.\nPlease type 'help' to review syntax for creating a new game.";
        } else {
            try {
                GameData gameToJoin = gamesMap.get(Integer.parseInt(params[0]));
                if (gameToJoin == null) {
                    return EscapeSequences.SET_TEXT_COLOR_RED + "game number: " + Integer.parseInt(params[0]) + " does not exist." +
                            "\nPlease type 'list' to list games again and run join with a valid game number.";
                }
                //observe game successful
                //if the playerColor string is still null, then it must be observer that was called.
                gameID = gameToJoin.gameID();
                playerColor = "observer";
                gameData = gameToJoin;
            } catch (NumberFormatException e) {
                return EscapeSequences.SET_TEXT_COLOR_RED + """
                        The game number you provided was invalid
                        please try again.""" + EscapeSequences.RESET_TEXT_COLOR;
            }
            return EscapeSequences.SET_TEXT_COLOR_GREEN + "You have successfully Observed the game " + params[0] + " as player: " +
                    currentAuthData.username() + EscapeSequences.RESET_TEXT_COLOR + "\n";
        }

    }

    public String loggedInHelp() {
        return """
                options:
                create <Name> - creates a new game called 'NAME'
                list - prints a list of all the games
                play <ID> [WHITE|BLACK] - join a game as either WHITE or BLACK
                observe <ID> - Observe a game as it happens
                logout - log yourself out
                quit - logs you out, and quits chess
                help - print possible commands again""";
    }
}
