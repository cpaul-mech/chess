package ui;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class OverallRepl {
    private LoginState loginState;
    private final LoggedOutClient loggedOutClient;
    private final LoggedInClient loggedInClient;

    public OverallRepl(String serverURL) {
        loginState = LoginState.LOGGED_OUT;
        loggedOutClient = new LoggedOutClient(serverURL);
        loggedInClient = new LoggedInClient();
    }

    private enum LoginState {
        LOGGED_IN,
        LOGGED_OUT
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
                if (loginState == LoginState.LOGGED_OUT) {
                    result = loggedOutClient.eval(line);
                } else {
                    result = loggedInClient.eval(line);
                }
                System.out.println(result);

            } catch (Exception e) {
                var errorMessage = e.getMessage();
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + errorMessage);
            }
        }

    }

    public void changeLoginState() {
        if (loginState == LoginState.LOGGED_IN) {
            loginState = LoginState.LOGGED_OUT;
        } else {
            loginState = LoginState.LOGGED_OUT;
        }
    }

    public void terminalArrows() {
        System.out.print(EscapeSequences.RESET_TEXT_COLOR + loginState.toString() + ": >>> ");
    }


}
