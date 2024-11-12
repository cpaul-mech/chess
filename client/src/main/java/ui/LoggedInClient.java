package ui;

public class LoggedInClient {
    private String authToken;
    private String serverUrl;
    private ServerFacade server;

    public LoggedInClient(String url, ServerFacade facade) {
        //when this object is created, it will always be null
        authToken = null;
        serverUrl = url;
        server = facade;
    }

    public void setAuthToken(String aT) {
        authToken = aT;
    }

    public String help() {
        return "";
    }

    public String eval(String line) {
        return "";
    }
}
