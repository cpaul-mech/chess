package ui;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class OverallRepl {
    private LoginState loginState;
    private final LoggedOutClient loggedOutClient;
    private final LoggedInClient loggedInClient;

    public OverallRepl() {
        loginState = LoginState.LOGGED_OUT;
        loggedOutClient = new LoggedOutClient();
        loggedInClient = new LoggedInClient();
    }

    private enum LoginState {
        LOGGED_IN,
        LOGGED_OUT
    }

    public void run() {
        //this will be called to run the repl
        System.out.println("-----You have entered the ChessGame Terminal-----\nType Help to get started.");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        String line = "";
        try {
            while (!result.equals("quit")) {
                terminalArrows();
                while (Objects.equals(line, "")) {
                    if (System.in.available() > 0) {
                        line = scanner.nextLine();
                    }
                }
                //need to parse the command given
                result = loggedOutClient.eval(line);
            }
        } catch (IOException e) {
            System.out.println("An Io Exception occurred, please try again.");
        }

    }

    public void terminalArrows() {
        System.out.println(loginState.toString() + ": >>> ");
    }


}
