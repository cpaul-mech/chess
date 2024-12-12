package ui;

import model.AuthData;
import ui.clientWebsocket.NotificationHandler;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.Locale;
import java.util.Scanner;

public class OverallRepl implements NotificationHandler {
    private UiState uiState;
    private final LoggedOutClient loggedOutClient;
    private final LoggedInClient loggedInClient;
    private AuthData userAuthData;
    private GamePlayClient gamePlayClient;
    private String playerColor;
    private BoardPrinter boardPrinter = new BoardPrinter();

    public OverallRepl(String serverURL) {
        uiState = UiState.LOGGED_OUT;
        loggedOutClient = new LoggedOutClient(serverURL);
        loggedInClient = new LoggedInClient(serverURL, loggedOutClient.getServer());
        gamePlayClient = new GamePlayClient(serverURL, this);
    }

    private enum UiState {
        LOGGED_IN,
        LOGGED_OUT,
        GAME_PLAY
    }

    public void run() {
        //this will be called to run the repl
        System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "-----You have entered the ChessGame " +
                "Terminal-----\n" + EscapeSequences.RESET_TEXT_COLOR +
                "Type" + EscapeSequences.SET_TEXT_COLOR_GREEN + " help" + EscapeSequences.RESET_TEXT_COLOR +
                " to get started.");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            terminalArrows();
            String line = scanner.nextLine();
            //need to parse the command given
            try {
                if (uiState == UiState.LOGGED_OUT) {
                    result = loggedOutClient.eval(line);
                    if (loggedOutClient.getCurrentUserAuthData() != null) {
                        //the login was successful, now we need to change the state and add the AuthData to the loggedin
                        //client
                        loggedInClient.setCurrentAuthData(loggedOutClient.getCurrentUserAuthData());
                        //change the state:
                        uiState = UiState.LOGGED_IN;
                    }
                } else if (uiState == UiState.LOGGED_IN) {
                    //need to check here for if the join command was successful
                    result = loggedInClient.eval(line);
                    if (loggedInClient.getCurrentAuthData() == null) {
                        loggedOutClient.deleteCurrentUserAuthData();
                        uiState = UiState.LOGGED_OUT;
                    }
                    if (loggedInClient.gameID != null) {
                        //the player has joined or observed a game.
                        uiState = UiState.GAME_PLAY;
                        this.playerColor = loggedInClient.playerColor.toUpperCase();
                        gamePlayClient.setPlayerColor(this.playerColor);
                        gamePlayClient.setGameID(loggedInClient.gameID);
                        gamePlayClient.setCurrentAuthData(loggedInClient.getCurrentAuthData());
                        gamePlayClient.setServer(loggedOutClient.getServer());
                        gamePlayClient.setGameData(loggedInClient.gameData);
                        gamePlayClient.initializeWSFacade();

                    }
                } else if (uiState == UiState.GAME_PLAY) {
                    result = gamePlayClient.eval(line);
                    if (gamePlayClient.getGameID() == null) {
                        //the player has left the game, transition to the postLogin UI
                        playerColor = null;
                        loggedInClient.playerColor = null;
                        loggedInClient.gameData = null;
                        loggedInClient.gameID = null;
                        uiState = UiState.LOGGED_IN;
                    }
                }
                System.out.println(result);

            } catch (Exception e) {
                var errorMessage = e.getMessage();
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + errorMessage);
            }
        }
        System.out.println("You have quit Chess, thanks for playing!!");

    }

    public void terminalArrows() {
        System.out.print(EscapeSequences.RESET_TEXT_COLOR + uiState.toString() + ": >>> ");
    }

    public void notify(NotificationMessage n, LoadGameMessage l, ErrorMessage e) {
        if (n != null) {
            System.out.println("\n" + EscapeSequences.SET_TEXT_COLOR_BLUE + n.message);
            terminalArrows();
        } else if (l != null) {
            switch (playerColor) {
                case "WHITE", "OBSERVER" -> {
                    System.out.println("\n" + boardPrinter.printWhiteGame(l.game));
                    //If I need to I can have the board printer accessed only by the GamePlayClient.
                    gamePlayClient.updateChessGame(l.game);
                }
                case "BLACK" -> {
                    System.out.println("\n" + boardPrinter.printBlackGame(l.game));
                    gamePlayClient.updateChessGame(l.game);
                }
                case null, default -> {
                    System.out.println("\nSOMETHING WENT WRONG with notify.");
                }
            }
            terminalArrows();
        } else if (e != null) {
            System.out.println("\n" + EscapeSequences.SET_TEXT_COLOR_RED + e.errorMessage);
            terminalArrows();
        }
    }


}
