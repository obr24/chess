package client.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    URI socketURI;
    Session session;
    NotificationHandler notificationHandler; // todo: double check how notificationhandler works

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.socketURI = socketURI;
            this.notificationHandler = notificationHandler;

        } catch (URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void connect(String authToken, Integer gameID) throws ResponseException {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case ERROR:
                            notificationHandler.notifyError(new Gson().fromJson(message, ErrorMessage.class));
                            break;
                        case NOTIFICATION:
                            notificationHandler.notifyNotification(new Gson().fromJson(message, NotificationMessage.class));
                            break;
                        case LOAD_GAME:
                            notificationHandler.notifyLoadGame(new Gson().fromJson(message, LoadGameMessage.class));
                            break;
                        case null, default:
                            notificationHandler.notify(new Gson().fromJson(message, ServerMessage.class));
                    }
                }
            });
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            sendCommand(command);
        } catch (Exception e) {
            throw new ResponseException(500, String.format("IsSue: %s", e.getMessage()));
        }
    }

    public void resign(String authToken, Integer gameID) throws ResponseException {
        try {
            sendCommand(new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID));
        } catch (IOException e) {
            throw new ResponseException(500, String.format("IssuE: %s", e.getMessage()));
        }
    }

    public void leave(String authToken, Integer gameID) throws ResponseException {
        try {
            sendCommand(new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID));
        } catch (IOException e) {
            throw new ResponseException(500, String.format("IsSuE: %s", e.getMessage()));
        }
    }

    private void sendCommand(UserGameCommand command) throws IOException {
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }
//
//    public void enterPetShop(String visitorName) throws ResponseException {
//        try {
//            var action = new Action(Action.Type.ENTER, visitorName);
//            this.session.getBasicRemote().sendText(new Gson().toJson(action));
//        } catch (IOException ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
