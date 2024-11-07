package ui;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class OverallRepl {
    LoginState loginState;

    public OverallRepl() {
        loginState = LoginState.LOGGED_OUT;
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

            }
        } catch (IOException e) {

        }

    }

    public void terminalArrows() {
        System.out.println(loginState.toString() + ": >>> ");
    }


}
