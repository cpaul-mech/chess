package ui;

import model.AuthData;
import server.websocket.Connection;
import ui.clientWebsocket.NotificationHandler;
import ui.clientWebsocket.WSFacade;

public class GamePlayClient {
    String url;
    private AuthData currentAuthData;
    private WSFacade ws;
    private String playerColor;
    private Integer gameID;
    private final NotificationHandler notificationHandler;

    public GamePlayClient(String url, NotificationHandler notificationHandler) {
        this.url = url;
        this.notificationHandler = notificationHandler;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public void initializeWSFacade() {
        try {
            ws = new WSFacade(url, notificationHandler);
            ws.gameID = this.gameID;
            //now it has all the information that it needs!!
            //gameID
        } catch (ServerException e) {
            throw new RuntimeException(e); //this is probably wrong.
        }
    }

    public String eval(String line) {
        
    }
}
