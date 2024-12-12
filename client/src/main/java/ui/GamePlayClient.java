package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import model.AuthData;
import model.GameData;
import ui.clientWebsocket.NotificationHandler;
import ui.clientWebsocket.WSFacade;

import java.util.Arrays;

public class GamePlayClient {
    String url;
    private AuthData currentAuthData;
    private WSFacade ws;
    private String playerColor;
    private Integer gameID;
    private final NotificationHandler notificationHandler;
    private BoardPrinter boardPrinter = new BoardPrinter();
    private ServerFacade server;
    private GameData gameData;


    public GamePlayClient(String url, NotificationHandler notificationHandler) {
        this.url = url;
        this.notificationHandler = notificationHandler;
    }

    public void initializeWSFacade() {
        try {
            ws = new WSFacade(url, notificationHandler);
            ws.connect(currentAuthData, gameID);//gameID
        } catch (ServerException e) {
            throw new RuntimeException(e); //this is probably wrong.
        }
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
                case "help" -> gamePlayHelp();
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "highlight" -> renderLegalMoves(params);
                case "move" -> makeMove(params);
                case "resign"-> resign();
                default -> EscapeSequences.SET_TEXT_COLOR_RED + "cmd: '" + cmd + "' was not understood.\n" +
                        EscapeSequences.RESET_TEXT_COLOR + gamePlayHelp();
            };
        }
    }

    private String resign() {
    }

    private String makeMove(String[] params) {
        //expecting two different strings, each of which should create valid chessPositions
        if (params == null || params.length < 2) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Not enough arguments given, please re-run help";
        }
        try {
            ChessPosition position1 = checkParameter(params[0]);
            ChessPosition position2 = checkParameter(params[1]);
            ChessMove moveToMake = new ChessMove(position1,position2,null);
            ChessGame game = gameData.game();
            game.makeMove(moveToMake);
            ws.makeMove(currentAuthData,gameID,moveToMake);
            updateChessGame(game);
            return EscapeSequences.SET_TEXT_COLOR_GREEN+String.format("%s was successful",moveToMake.toString());

        } catch (BadInputException e) {
            return EscapeSequences.SET_TEXT_COLOR_RED + e.getMessage();
        } catch (InvalidMoveException e) {
            return EscapeSequences.SET_TEXT_COLOR_RED+"the proposed move is invalid\n" +
                    e.getMessage();
        } catch (ServerException e) {
            return EscapeSequences.SET_TEXT_COLOR_RED+"An unexpected server exception occurred.\n" +
                    e.getMessage();
        }
        //need to split up the two characters
    }

    private ChessPosition checkParameter(String positionString) throws BadInputException {
        if (positionString.length() < 2) {
            throw new BadInputException(String.format("chess position could not be created from '%s'", positionString));
        }
        try {
            String firstCharacter = String.valueOf(positionString.charAt(0)); //should be a number
            char secondCharacter = positionString.charAt(1);
            int row = Integer.parseInt(firstCharacter);
            //both of the characters are good!!
            //convert the char to an integer value
            int col = secondCharacter - 'a'+1;
            if(row<1||col<1||row>8||col>8){
                throw new BadInputException(String.format("chess position could not be created from '%s'\n" +
                        "Please try again, ensure that each chessPosition is formatted as\n" +
                        "<integer(representing row)><character(representing column)", positionString));
            }
            return new ChessPosition(row,col);
        } catch (NumberFormatException e) {
            throw new BadInputException(String.format("chess position could not be created from '%s'\n" +
                    "Please try again, ensure that each chessPosition is formatted as\n" +
                    "<integer(representing row)><character(representing column)", positionString));
        }
    }

    private String leave() {
        //needs to disconnect!!
        try {
            ws.leave(this.currentAuthData, this.gameID);
            playerColor = null; //resetting all the variables.
            gameID = null;
            gameData = null;
            ws = null;
            return String.format("player '%s' has left successfully", currentAuthData.username())
                    + "\nReturning to loggedIn UI";
        } catch (ServerException e) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Error " + e.getrCode() + ", " + e.getMessage();
        }
    }

    private String redraw() {
        //actually I shouldn't need to get the game again, I just need to update the game within the gameplay client every time a load game message is sent.
        return switch (playerColor) {
            case "WHITE", "OBSERVER" -> boardPrinter.printWhiteGame(gameData.game());
            case "BLACK" -> boardPrinter.printBlackGame(gameData.game());
            case null, default -> "something went wrong while attempting to redraw the board.";
        };
    }

    private String renderLegalMoves(String[] params) {
        if (params == null || params.length < 1) {
            return EscapeSequences.SET_TEXT_COLOR_RED + "Not enough arguments given, please re-run help";
        }
        try{
            ChessPosition position1 = checkParameter(params[0]);
            ChessGame game = gameData.game();
            var validMoves = game.validMoves(position1);
            String legalMovesChessGamePrinted()
            return EscapeSequences.SET_TEXT_COLOR_GREEN+String.format("Rendering legal moves for: '%s'",game.getBoard().getPiece(position1));
        } catch (BadInputException e) {
            return EscapeSequences.SET_TEXT_COLOR_RED + e.getMessage();
        }
    }


    public String gamePlayHelp() {
        String helpString = """
                'help' - dislays all possible commands.
                'redraw' - redraws the saved chess board
                'leave' - removes user from the game (opposite of join)
                'move <two character position1> <two character position2>' - makes a move if the current user is playing a game.
                'resign' -user concedes the game and loses the match
                'highlight <two character position>' - highlights green all the squares where the player can move. """;
        return helpString;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public void setCurrentAuthData(AuthData authData) {
        this.currentAuthData = authData;
    }

    public void setServer(ServerFacade server) {
        this.server = server;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public void updateChessGame(ChessGame game) {
        GameData upGameData = new GameData(this.gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
        this.gameData = upGameData;
    }

    public Integer getGameID() {
        return gameID;
    }
}
