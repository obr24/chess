package client;

import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import ui.PrintBoard;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Arrays;

import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class InGameClient {
    private final ServerFacade facade;
    private final WebSocketFacade wsFacade;
    private final ClientState clientState;
    private boolean WebSocketInitialized = false;

    public InGameClient(WebSocketFacade wsFacade, ServerFacade facade, ClientState clientState) {
        this.facade = facade;
        this.wsFacade = wsFacade;
        this.clientState = clientState;
    }

    public void initializeWebSocket() {
        try {
            wsFacade.connect(clientState.getAuthToken(), clientState.getGameID());
            WebSocketInitialized = true;
        } catch (ResponseException e) {
            System.out.println("big issue connecting in inGameclient");
            throw new RuntimeException(e);
        }
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> highlight();
                default -> unknownInput();
            };
//        } catch (ResponseException ex) {
        } catch (Exception ex) {
            return String.format("error in prelogin: %s", ex.getMessage());
        }
    }

    public String redraw() {
        return PrintBoard.print(clientState.getChessGame(), clientState.getPlayerColor().toString());
    }

    public String leave() throws ResponseException {
        wsFacade.leave(clientState.getAuthToken(), clientState.getGameID());
        clientState.setChessGame(null);
        clientState.setGameID(null);
        clientState.setPlayerColor(null);
        clientState.setObservingGameID(null);
        clientState.setUserState(State.POST_LOGIN);
        return "Leaving now";
    }

    public String move(String[] params) {
        return String.format("Attempting move with params: %s", Arrays.toString(params));
    }

    public String resign() throws ResponseException {
        // todo add in reset of client state?
        wsFacade.resign(clientState.getAuthToken(), clientState.getGameID());
        return "You have resigned from the game.";
    }

    public String highlight() {
        return "Highlighting legal moves...";
    }

    public String help() {
        return """
            Options:
              Help: "help"
              Redraw board: "redraw"
              Leave game: "leave"
              Make move: "move <from> <to> <optional promotion>" (e.g. f5 e4 q)
              Resign: "resign"
              Highlight legal moves: "highlight"
            """;
    }

    public String unknownInput() {
        return String.format("Unknown input:\n%s", help());
    }

}
