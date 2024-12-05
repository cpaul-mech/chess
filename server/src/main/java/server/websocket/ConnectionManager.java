package server.websocket;

import com.google.gson.Gson;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    private final Gson serializer = new Gson();

    public void add(Connection conn) {
        connections.put(conn.userName, conn);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcastToAllConnections(String excludeVisitorName, ServerMessage serverMessage) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.userName.equals(excludeVisitorName)) {
                    c.send(serializer.toJson(serverMessage));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.userName);
        }
    }

    public void broadcastToAllInGame(Integer relevantGameID, String excludeUsername, ServerMessage serverMessage) throws IOException {
        //first, we find the gameID that we need to check for, and send it to all connections with that gameID except for the excluded one.
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {//checking to add to the removeList.
                if (c.gameID == relevantGameID && !c.userName.equals(excludeUsername)) {
                    c.send(serializer.toJson(serverMessage));
                }
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.userName);
        }
    }
}

