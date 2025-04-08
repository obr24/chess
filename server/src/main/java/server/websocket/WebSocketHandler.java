package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.ServiceException;
import websocket.commands.UserGameCommand;
import service.GameService.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
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
            case MAKE_MOVE -> makeMove(session, command.getAuthToken(), command.getGameID(), command.getMove());
            case RESIGN -> resign(session, command.getAuthToken(), command.getGameID());
            case LEAVE -> leave(session, command.getAuthToken(), command.getGameID());
            case null, default -> unknown();
        }
    }

    private void leave(Session session, String authToken, Integer gameID) {
        try {
            String username = authDAO.getAuth(authToken).username();
            GameData gameData = gamesDAO.getGame(gameID);
            if (isObserver(username, gamesDAO.getGame(gameID))) {
                connections.sendMessageToGameExceptUser(gameID, username, new Gson().toJson(new NotificationMessage(String.format("Observer %s has left", username))));
                connections.remove(username);
                return;
            } else if (gameData.whiteUsername().equals(username)) {
                gamesDAO.updateGame(WHITE, gameID, null);
            } else if (gameData.blackUsername().equals(username)) {
                gamesDAO.updateGame(BLACK, gameID, null);
            }
            connections.sendMessageToGameExceptUser(gameID, username, new Gson().toJson(new NotificationMessage(String.format("Player %s has left", username))));
            connections.remove(username);
        } catch (Exception e) {
            try {
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage(String.format("%s: %s\n%s", "bad something", e.getMessage(), e.getStackTrace()))));
            } catch (IOException ex) {
                System.out.println("big error in leave over here");
                throw new RuntimeException(ex);
            }
        }
    }

    private void resign(Session session, String authToken, Integer gameID) {
        try {
            String username = authDAO.getAuth(authToken).username();
            if (isObserver(username, gamesDAO.getGame(gameID))) {
                connections.sendMessageToUser(username, new Gson().toJson(new ErrorMessage("Error: you cannot resign as observer")));
                return;
            }
            ChessGame game = gamesDAO.getGame(gameID).game();
            if (game.isOver()) {
                connections.sendMessageToUser(username, new Gson().toJson(new ErrorMessage("Error: cannot resign after game is over")));
                return;
            }
            game.setOver(true);
            gamesDAO.setGame(gameID, game);
            connections.sendMessageToGame(gameID, new Gson().toJson(new NotificationMessage(String.format("player %s resigned", username))));
        } catch (Exception e) {
            try {
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage(String.format("%s: %s\n%s", "bad something", e.getMessage(), e.getStackTrace()))));
            } catch (IOException ex) {
                System.out.println("big error in resign over here");
                throw new RuntimeException(ex);
            }
        }
    }

    private boolean isObserver(String username, GameData gameData) {
        return !Objects.equals(gameData.whiteUsername(), username) && !Objects.equals(gameData.blackUsername(), username);
    }

    private void makeMove(Session session, String authToken, Integer gameID, ChessMove move) {
        try {
            ChessGame game = gamesDAO.getGame(gameID).game();

            if (isGameOver(game)) {
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage("The game is over")));
                return;
            }

            String username = authDAO.getAuth(authToken).username();
            ChessGame.TeamColor curColor = game.getTeamTurn();
            ChessGame.TeamColor pieceColor = game.getBoard().getPiece(move.getStartPosition()).getTeamColor();

            boolean isWhiteTurn = curColor.equals(WHITE) &&
                    gamesDAO.getGame(gameID).whiteUsername().equals(username) &&
                    pieceColor.equals(WHITE);

            boolean isBlackTurn = curColor.equals(BLACK) &&
                    gamesDAO.getGame(gameID).blackUsername().equals(username) &&
                    pieceColor.equals(BLACK);

            if (!isWhiteTurn && !isBlackTurn) {
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Not your turn or not your piece")));
                return;
            }
            game.makeMove(move);
            gamesDAO.setGame(gameID, game);
            connections.sendMessageToGame(gameID, new Gson().toJson(new LoadGameMessage(
                    game)));
            connections.sendMessageToGameExceptUser(gameID, username, new Gson().toJson(
                    new NotificationMessage(String.format("%s made move %s", username, move.toString()))));
            inCheckorStalemate(gameID);
        } catch (Exception e) {
            try {
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage(String.format("%s: %s\n%s", "bad something", e.getMessage(), e.getStackTrace()))));
            } catch (IOException ex) {
                System.out.println("big error over here");
                throw new RuntimeException(ex);
            }
        }
    }

    private boolean isGameOver(ChessGame game) throws DataAccessException {
        return game.isOver() || game.isInCheckmate(WHITE) || game.isInCheckmate(BLACK) ||
                game.isInStalemate(WHITE) || game.isInStalemate(BLACK);
    }

    private void inCheckorStalemate(Integer gameID) throws DataAccessException {
        if (gamesDAO.getGame(gameID).game().isInCheck(WHITE) || gamesDAO.getGame(gameID).game().isInCheck(BLACK)) {
            connections.sendMessageToGame(gameID, new Gson().toJson(new NotificationMessage("game in check")));
        } else if (gamesDAO.getGame(gameID).game().isInCheckmate(WHITE) || gamesDAO.getGame(gameID).game().isInCheckmate(BLACK)) {
            connections.sendMessageToGame(gameID, new Gson().toJson(new NotificationMessage("game in checkmate")));
        } else if (gamesDAO.getGame(gameID).game().isInStalemate(WHITE) || gamesDAO.getGame(gameID).game().isInStalemate(BLACK)) {
            connections.sendMessageToGame(gameID, new Gson().toJson(new NotificationMessage("game in stalemate")));
        }
    }

    private void connect(Session session, String authToken, Integer gameID) {
        try {
            String username = authDAO.getAuth(authToken).username();
            connections.add(username, gameID, session);
            connections.sendMessageToUser(username, new Gson().toJson(new LoadGameMessage(gamesDAO.getGame(gameID).game())));

            String playerColor;
            if (gamesDAO.getGame(gameID).whiteUsername().equals(username)) {
                playerColor = "white";
            } else if (gamesDAO.getGame(gameID).whiteUsername().equals(username)) {
                playerColor = "black";
            } else {
                playerColor = "an observer";
            }

            connections.sendMessageToGameExceptUser(gameID, username, new Gson().toJson(
                    new NotificationMessage(String.format("%s joined game as %s", username, playerColor))));
        } catch (DataAccessException e) {
            try {
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage("bad authentication")));
            } catch (IOException ex) {
                System.out.println("big error over here");
                throw new RuntimeException(ex);
            }
        }
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