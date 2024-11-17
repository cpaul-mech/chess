package ui;

import model.AuthData;

import java.util.Scanner;

public class OverallRepl {
    private LoginState loginState;
    private final LoggedOutClient loggedOutClient;
    private final LoggedInClient loggedInClient;
    private AuthData userAuthData;

    public OverallRepl(String serverURL) {
        loginState = LoginState.LOGGED_OUT;
        loggedOutClient = new LoggedOutClient(serverURL);
        loggedInClient = new LoggedInClient(serverURL, loggedOutClient.getServer());
    }

    private enum LoginState {
        LOGGED_IN,
        LOGGED_OUT
    }

    public void run() {
        //this will be called to run the repl
        System.out.println(loggedInClient.printBlackGameSample());
        System.out.print("\n\n");
        System.out.println(loggedInClient.printWhiteGameSample());
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
                if (loginState == LoginState.LOGGED_OUT) {
                    result = loggedOutClient.eval(line);
                    if (loggedOutClient.getCurrentUserAuthData() != null) {
                        //the login was successful, now we need to change the state and add the AuthData to the loggedin
                        //client
                        loggedInClient.setCurrentAuthData(loggedOutClient.getCurrentUserAuthData());
                        //change the state:
                        loginState = LoginState.LOGGED_IN;
                    }
                } else {
                    result = loggedInClient.eval(line);
                    if (loggedInClient.getCurrentAuthData() == null) {
                        loggedOutClient.deleteCurrentUserAuthData();
                        loginState = LoginState.LOGGED_OUT;
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
        System.out.print(EscapeSequences.RESET_TEXT_COLOR + loginState.toString() + ": >>> ");
    }


}
