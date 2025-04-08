package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
//import messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, Integer gameID, Session session) {
        var connection = new Connection(username, gameID, session);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void sendMessageToUser(String username, String msg) {
        cleanupConnections();
        try {
            connections.get(username).send(msg);
        } catch (IOException e) {
            System.out.println("dfsasfdasfdslkj");
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToGame(Integer gameID, String msg) {
        cleanupConnections();
        for (Map.Entry<String, Connection> entry : connections.entrySet()) {
            Connection connection = entry.getValue();
            if (connection.gameID.equals(gameID)) {
                try {
                    connection.send(msg);
                } catch (IOException e) {
                    System.out.println("safdafsk: ");
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void sendMessageToGameExceptUser(Integer gameID, String username, String msg) {
        cleanupConnections();
        for (Map.Entry<String, Connection> entry : connections.entrySet()) {
            Connection connection = entry.getValue();
            if (connection.gameID.equals(gameID) && !(connection.username.equals(username))) {
                try {
                    connection.send(msg);
                } catch (IOException e) {
                    System.out.println("safdafsk: ");
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void cleanupConnections() {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (!c.session.isOpen()) {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }
}
