package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoggedInClient {
    private AuthData currentAuthToken;
    private final ServerFacade server;
    private Map<Integer, GameData> gamesMap = new HashMap<>();

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
                JoinGameInput joinGameInput = new JoinGameInput(params[1], gameToJoin.gameID());
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
                    currentAuthToken.username() + EscapeSequences.RESET_TEXT_COLOR + "\n" +
                    printWhiteGameSample() + "\n" + printBlackGameSample();
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

    public String list() {
        List<GameData> gamesList = null;
        try {
            gamesList = (List<GameData>) server.listGames(currentAuthToken);
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
        String username = currentAuthToken.username();
        currentAuthToken = null; //reset the authData so that I can read that in the repl.
        return String.format("Logging '%s' out.", username) +
                "\nrerun 'help' for commands again if needed.";
    }

    public String observe(String[] params) {
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
            } catch (NumberFormatException e) {
                return EscapeSequences.SET_TEXT_COLOR_RED + """
                        The game number you provided was invalid
                        please try again.""" + EscapeSequences.RESET_TEXT_COLOR;
            }
            //join game successful
            return EscapeSequences.SET_TEXT_COLOR_GREEN + "You have successfully Observed the game " + params[0] + " as player: " +
                    currentAuthToken.username() + EscapeSequences.RESET_TEXT_COLOR + "\n" +
                    printWhiteGameSample() + "\n" + printBlackGameSample();
        }

    }

    public String printBlackGameSample() {
        ChessGame chessGame = new ChessGame();
        //first print the game from the perspective of the white team,
        var board = chessGame.getBoard();
        StringBuilder gameString = new StringBuilder();
        for (int r = 0; r < 10; r++) {
            if (r == 0 || r == 9) {
                gameString.append(topBottomBlackPerspectiveString());
            } else {
                gameString.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + String.format(" %d ", r));
                if (r % 2 != 0) {
                    gameString.append(printWhiteFirstRow(r, board));
                } else {
                    gameString.append(printBlackFirstRow(r, board));
                }
                gameString.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + String.format(" %d ", r) + EscapeSequences.RESET_BG_COLOR + "\n");
            }
        }
        return gameString.toString();
    }

    public String printWhiteGameSample() {
        ChessGame chessGame = new ChessGame();
        //first print the game from the perspective of the white team,
        var board = chessGame.getBoard();
        StringBuilder gameString = new StringBuilder();
        for (int r = 9; r >= 0; r--) {
            if (r == 0 || r == 9) {
                gameString.append(topBottomWhitePerspective());
            } else {
                gameString.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + String.format(" %d ", r));
                if (r % 2 == 0) {
                    gameString.append(printWhiteFirstRow(r, board));
                } else {
                    gameString.append(printBlackFirstRow(r, board));
                }
                gameString.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + String.format(" %d ", r) + EscapeSequences.RESET_BG_COLOR + "\n");
            }
        }
        return gameString.toString();
    }

    public String printWhiteFirstRow(int r, ChessBoard board) {
        StringBuilder row = new StringBuilder();
        for (int c = 1; c < 9; c++) {
            if (c % 2 != 0) {//column number is odd,
                row.append(EscapeSequences.SET_BG_COLOR_WHITE + " ");
                row.append(stringizeChessPiece(r, c, board));
                row.append(" ");
            } else {
                row.append(EscapeSequences.SET_BG_COLOR_BLACK + " ");
                row.append(stringizeChessPiece(r, c, board));
                row.append(" ");
            }
        }
        return row.toString();
    }

    public String printBlackFirstRow(int r, ChessBoard board) {
        StringBuilder row = new StringBuilder();
        for (int c = 1; c < 9; c++) {
            if (c % 2 == 0) {//column number is odd,
                row.append(EscapeSequences.SET_BG_COLOR_WHITE + " ");
                row.append(stringizeChessPiece(r, c, board));
                row.append(" ");
            } else {
                row.append(EscapeSequences.SET_BG_COLOR_BLACK + " ");
                row.append(stringizeChessPiece(r, c, board));
                row.append(" ");
            }
        }
        return row.toString();
    }

    public String stringizeChessPiece(int r, int c, ChessBoard board) {
        ChessPosition p = new ChessPosition(r, c);
        var piece = board.getPiece(p);
        if (piece == null) {
            return " ";
        }
        var pieceType = piece.getPieceType();
        String pieceColor;
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            pieceColor = EscapeSequences.SET_TEXT_COLOR_RED;
        } else {
            pieceColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
        }
        switch (pieceType) {
            case ROOK -> {
                return pieceColor + "R";
            }
            case KNIGHT -> {
                return pieceColor + "N";
            }
            case BISHOP -> {
                return pieceColor + "B";
            }
            case QUEEN -> {
                return pieceColor + "Q";
            }
            case PAWN -> {
                return pieceColor + "P";
            }
            case KING -> {
                return pieceColor + "K";
            }
            default -> {
                return " ";
            }
        }
    }


    public String topBottomBlackPerspectiveString() {
        StringBuilder rowString = new StringBuilder();
        rowString.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ");
        for (char l = 'h'; l >= 'a'; l--) {
            rowString.append(" " + l + " ");
        }
        rowString.append("   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n");
        return rowString.toString();
    }

    public String topBottomWhitePerspective() {
        StringBuilder rowString = new StringBuilder();
        rowString.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ");
        for (char l = 'a'; l <= 'h'; l++) {
            rowString.append(" " + l + " ");
        }
        rowString.append("   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n");
        return rowString.toString();
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
