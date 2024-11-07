package ui;

public class OverallRepl {
    LoginState loginState;

    public OverallRepl() {
        loginState = LoginState.LOGGED_OUT;
    }

    private enum LoginState {
        LOGGED_IN,
        LOGGED_OUT
    }


}
