package ui;

import model.AuthData;
import server.Server;
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
                default -> EscapeSequences.SET_TEXT_COLOR_RED + "cmd: '" + cmd + "' was not understood.\n" +
                        EscapeSequences.RESET_TEXT_COLOR + gamePlayHelp();
            };
        }
    }

    private String redraw() {
        server.
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
    public void setServer(ServerFacade server){
        this.server = server;
    }
}
