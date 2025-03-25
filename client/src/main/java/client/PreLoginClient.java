package client;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.RequestsAndResults;
import ui.PrintBoard;

import java.util.Arrays;
import java.util.Objects;

public class PreLoginClient {
    private final ServerFacade facade;
    private final ClientState clientState;

    public PreLoginClient(ServerFacade facade, ClientState clientState) {
        this.facade = facade;
        this.clientState = clientState;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                case "help" -> help();
                // todo remove:
                case "b" -> boardTest();
                case "v" -> boardTestBlack();
                default -> unknownInput();
            };
        } catch (ResponseException ex) {
            return String.format("error in prelogin: %s", ex.getMessage());
        }
    }

    // todo: remove this function
    public String boardTest() {
        return new PrintBoard().print(new GameData(999, "whiteUs", "blk", "gme",
                new ChessGame()), "observer");
    }
    public String boardTestBlack() {
        return new PrintBoard().print(new GameData(999, "whiteUs", "blk", "gme",
                new ChessGame()), "black");
    }
    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            RequestsAndResults.LoginResult loginResult;
            try {
                loginResult = facade.login(new RequestsAndResults.LoginRequest(params[0], params[1]));
                clientState.setUserState(State.POST_LOGIN);
                clientState.setAuthToken(loginResult.authToken());
            } catch (ResponseException e) {
                throw new ResponseException(400, String.format("error in register: %s", e.getMessage()));
            }
            return String.format("You logged in as %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            RequestsAndResults.RegisterResult registerResult;
            try {
                registerResult = facade.register(new RequestsAndResults.RegisterRequest(params[0], params[1], params[2]));
                clientState.setUserState(State.POST_LOGIN);
                clientState.setAuthToken(registerResult.authToken());
            } catch (ResponseException e) {
                throw new ResponseException(400, String.format("error in register: %s", e.getMessage()));
            }
            return String.format("You registered as %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String help() {
        // todo add in for other clients (like if logged in or not)
        return """
                Options:
                Login: "login" <USERNAME> <PASSWORD>
                Register: "register" <USERNAME> <PASSWORD> <EMAIL>
                Exit: "quit"
                Help: "help"
                """;
    }

    public String unknownInput() {
        return String.format("Unknown input:\n%s", help());
    }
}
