package client.websocket;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public interface NotificationHandler {
    void notify(ServerMessage message);

    void notifyError(ErrorMessage errorMessage);

    void notifyNotification(NotificationMessage notificationMessage);

    void notifyLoadGame(LoadGameMessage loadGameMessage);
}