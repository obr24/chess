package server.websocket;

import com.google.gson.Gson;
import dataaccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.ServiceException;
import websocket.commands.UserGameCommand;
import service.GameService.*;
import websocket.messages.ServerMessage;

import static chess.ChessGame.TeamColor.WHITE;
import static service.GameService.joinGame;

@WebSocket
public class WebSocketHandler {
    private ConnectionManager connections = new ConnectionManager();

    private GamesDAO gamesDAO = null;
    private UserDAO userDAO = null;
    private AuthDAO authDAO = null;

    public WebSocketHandler() {
        connections = new ConnectionManager();
        try {
            gamesDAO = new SqlGamesDAO();
            userDAO = new SqlUserDAO();
            authDAO = new SqlAuthDAO();
        } catch (Exception e) {
            System.out.printf("Error in websockethandler: %s%n", e.getMessage());
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(session, command.getAuthToken(), command.getGameID());
            case null, default -> unknown();
        }
    }

    private void connect(Session session, String authToken, Integer gameID) {
        try {
            connections.add(authDAO.getAuth(authToken).username(), gameID, session);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        ConnectionManager.send
        session.getRemote().sendString(msg);
    }

    private void unknown() {
        return;
    }
//    private void enter(String visitorName, Session session) throws IOException {
//        connections.add(visitorName, session);
//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(Notification.Type.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
//    }

//    private void exit(String visitorName) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
//    }

//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new Notification(Notification.Type.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
}