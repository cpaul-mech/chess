package server.websocket;

import server.Server;
//import spark.Session;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String userName;
    public Session session;

    public Connection(String thisUsername, Session thisSession) {
        userName = thisUsername;
        session = thisSession;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
