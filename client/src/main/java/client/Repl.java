package client;

import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import com.google.gson.Gson;
import exception.ResponseException;
import ui.PrintBoard;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {

    private final ServerFacade facade;
    private final PreLoginClient preLoginClient;
    private final PostLoginClient postLoginClient;
    private final InGameClient inGameClient;
    private ClientState clientState;

    private final WebSocketFacade wsFacade;
    public Repl(String serverUrl) {
        facade = new ServerFacade("http://127.0.0.1:8080");
        try {
            wsFacade = new WebSocketFacade("http://127.0.0.1:8080", this); // todo double check url
        } catch (ResponseException e) {
            System.out.println("very grand issue in repl:");
            throw new RuntimeException(e);
        }

        clientState = new ClientState();
        preLoginClient = new PreLoginClient(facade, clientState);
        inGameClient = new InGameClient(wsFacade, facade, clientState);
        postLoginClient = new PostLoginClient(inGameClient, facade, clientState);
    }

    public void run() {
        System.out.println("Welcome to Chess. Sign in to start.");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = switch (clientState.getUserState()) {
                    case PRE_LOGIN -> preLoginClient.eval(line);
                    case POST_LOGIN -> postLoginClient.eval(line);
                    case IN_GAME -> inGameClient.eval(line);
                };
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Exception e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" +  resetAll() + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    private String resetAll() {
        return RESET_TEXT_COLOR + RESET_BG_COLOR;
    }

    // finish overloading loadgame and notification
    public void notifyError(ErrorMessage message) {
        handleError(message);
    }

    public void notifyLoadGame(LoadGameMessage message) {
        handleLoadGame(message);
    }

    public void notifyNotification(NotificationMessage message) {
        handleNotification(message);
    }

    @Override
    public void notify(ServerMessage message) {
        handleUnknown(message);
        printPrompt();
    }

    private void handleLoadGame(LoadGameMessage message) {
        clientState.setChessGame(message.getGame());
        System.out.println("\n" + PrintBoard.print(clientState.getChessGame(), clientState.getPlayerColor().toString()));
        printPrompt();
    }

    private void handleNotification(NotificationMessage message) {
        System.out.println(message.getMessage());
        printPrompt();
    }

    private void handleError(ErrorMessage message) {
        System.out.println("Error: " + message.toString());
        printPrompt();
    }

    private void handleUnknown(ServerMessage message) {
        System.out.println("Unknown Message Type: " + message);
        printPrompt();
    }
}
