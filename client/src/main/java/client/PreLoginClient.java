package client;

import exception.ResponseException;
import model.RequestsAndResults;

import java.util.Arrays;

public class PreLoginClient {
    private final ServerFacade facade;
    private final String serverUrl;

    public PreLoginClient(String serverUrl) {
        facade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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
                default -> unknownInput();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        return "login not implemented";
//        if (params.length >= 1) {
//            state = State.SIGNEDIN;
//            visitorName = String.join("-", params);
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(visitorName);
//            return String.format("You signed in as %s.", visitorName);
//        }
//        throw new ResponseException(400, "Expected: <yourname>");
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            RequestsAndResults.RegisterResult registerResult;
            try {
                registerResult = facade.register(new RequestsAndResults.RegisterRequest(params[0], params[1], params[2]));
            } catch (ResponseException e) {
                throw new ResponseException(400, e.getMessage());
            }
//            state = State.SIGNEDIN;
//            visitorName = String.join("-", params);
//            ws = new WebSocketFacade(serverUrl, notificationHandler);
//            ws.enterPetShop(visitorName);
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
