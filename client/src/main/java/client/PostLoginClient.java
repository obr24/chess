package client;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.RequestsAndResults;
import ui.PrintBoard;

import java.util.*;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class PostLoginClient {
    private final ServerFacade facade;
    private final ClientState clientState;
    private final PrintBoard printBoard;
    private final InGameClient inGameClient;

    public PostLoginClient(InGameClient inGameClient, ServerFacade facade, ClientState clientState) {
        this.facade = facade;
        this.clientState = clientState;
        this.printBoard = new PrintBoard();
        this.inGameClient = inGameClient;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "play" -> playGame(params);
                case "observe" -> observeGame(params);
                default -> unknownInput();
            };
        } catch (ResponseException ex) {
            return String.format("error in prelogin: %s", ex.getMessage());
        }

    }

    private String logout() throws ResponseException {
        try {
            RequestsAndResults.LogoutResult result = facade.logout(new RequestsAndResults.LogoutRequest(clientState.getAuthToken()));
            clientState.setUserState(State.PRE_LOGIN);
            return String.format("You logged out.");
        } catch (ResponseException e) {
            throw new ResponseException(400, String.format("error in logout: %s", e.getMessage()));
        }
    }

    private String createGame(String[] params) throws ResponseException {
        if (params.length == 1) {
            RequestsAndResults.CreateResult createResult;
            try {
                createResult = facade.create(new RequestsAndResults.CreateRequest(clientState.getAuthToken(), params[0]));
            } catch (ResponseException e) {
                throw new ResponseException(400, String.format("error in register: %s", e.getMessage()));
            }
            return String.format("You created new game: %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <GAME NAME>");
    }

    private String listGames() throws ResponseException {
        try {
            initializeGamesList();

            String outputString = "";
            for (Map.Entry<Integer, GameData> entry : clientState.getClientGameIdtoRealGameId().entrySet()) {
                outputString = String.format("%s\n%d: %s, white: %s, black: %s", outputString,
                        entry.getKey(), entry.getValue().gameName(), entry.getValue().whiteUsername(),
                        entry.getValue().blackUsername());
            }
            return String.format("All games:\n%s", outputString);
        } catch (ResponseException e) {
            throw new ResponseException(400, String.format("error in list games: %s", e.getMessage()));
        }
    }

    private void initializeGamesList() throws ResponseException {
        RequestsAndResults.ListResult result = facade.list(new RequestsAndResults.ListRequest(clientState.getAuthToken()));

        HashMap<Integer, GameData> gamesHash = new HashMap<>();
        int i = 0;
        for (GameData game: result.games()) {
            gamesHash.put(++i, game);
        }

        clientState.setClientGameIdtoRealGameId(gamesHash);
    }

    private String playGame(String[] params) throws ResponseException {
        if (params.length == 2) {
            RequestsAndResults.JoinResult joinResult;
            try {
                joinResult = facade.join(new RequestsAndResults.JoinRequest(clientState.getAuthToken(), params[1],
                        getRealGameId(Integer.parseInt(params[0]))));
                clientState.setGameID(joinResult.gameID());
                clientState.setPlayerColor(parseStringAsColor(params[1]));
                clientState.setUserState(State.IN_GAME); // add in phase 6
                inGameClient.initializeWebSocket();
            } catch (ResponseException e) {
                throw new ResponseException(400, String.format("error in play: %s", e.getMessage()));
            } catch (Exception e) {
                throw new ResponseException(400, "Bad input, expected number");
            }
            return String.format("You joined game: %s as color %s.", params[0], params[1]);
        }
        throw new ResponseException(400, "Expected: <GAME NUMBER> <COLOR>");
    }

    private ChessGame.TeamColor parseStringAsColor(String colorString) throws ResponseException {
        if (Objects.equals(colorString.toLowerCase(), "white")) {
            return WHITE;
        } else if (Objects.equals(colorString.toLowerCase(), "black")) {
            return BLACK;
        }
        throw new ResponseException(400, "bad color");
    }

    private Integer getRealGameId(Integer clientGameId) throws ResponseException {
        try {
            return clientState.getClientGameIdtoRealGameId().get(clientGameId).gameID();
        } catch (Exception e) {
            throw new ResponseException(500, "invalid game id");
        }
    }

    private GameData getGameData(Integer clientGameID)throws ResponseException {
        try {
            return clientState.getClientGameIdtoRealGameId().get(clientGameID);
        } catch (Exception e) {
            throw new ResponseException(500, "invalid game id");
        }

    }

    private String observeGame(String[] params) throws ResponseException {
        try {
            clientState.setObservingGameID(getRealGameId(Integer.parseInt(params[0])));
//            clientState.setUserState(State.IN_GAME);
            return String.format("observing game: %s\n%s", params[0],PrintBoard.print(
                    getGameData(Integer.parseInt(params[0])).game(), "observer"));
        } catch (Exception e) {
            throw new ResponseException(500, "bad input, expected numer");
        }
    }

    private String unknownInput() {
        return "Unknown command. Type 'help' for a list of available commands.";
    }

    public String help() {
        return """
                Options:
                Help: "help"
                Logout: "logout"
                Create Game: "create" <GAME NAME>
                List Games: "list"
                Play Game: "play" <GAME NUMBER> <COLOR>
                Observe Game: "observe" <GAME NUMBER>
                """;
    }
}
