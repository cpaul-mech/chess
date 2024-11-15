package ui;

import model.AuthData;

public class LoggedInClient {
    private AuthData currentAuthToken;
    private String serverUrl;
    private ServerFacade server;

    public LoggedInClient(String url, ServerFacade facade) {
        //when this object is created, it will always be null
        currentAuthToken = null;
        serverUrl = url;
        server = facade;
    }

    public void setCurrentAuthData(AuthData aD) {
        currentAuthToken = aD;
    }

    public String loggedInhelp() {
        return "";
    }

    public String eval(String line) {
        return "";
    }
}
