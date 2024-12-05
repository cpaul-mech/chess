package server.websocket;

//import spark.Session;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String userName;
    public Session session;
    public Role role;
    public int gameID;

    public enum Role {
        WHITE,
        BLACK,
        OBSERVER
    }

    public Connection(String thisUsername, Session thisSession, Role role, Integer gameID) {
        userName = thisUsername;
        session = thisSession;
        this.role = role;
        this.gameID = gameID;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}
