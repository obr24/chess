package model;

import java.util.Collection;

public class RequestsAndResults {
    public record RegisterRequest(String username, String password, String email) {}
    public record RegisterResult(String username, String authToken) {}
    public record LoginRequest(
            String username,
            String password) {}
    public record LoginResult(
            String username,
            String authToken) {}
    public record LogoutRequest(String authToken) {}
    public record LogoutResult() {}

    public record ClearRequest() {}
    public record ClearResult() {}

    public record FailureResponse(int errorCode, String message) {}

    public record CreateRequest(String authToken, String gameName) {}
    public record CreateResult(int gameID) {}
    public record JoinRequest(String authToken, String playerColor, int gameID) {}
    public record JoinResult(int gameID) {}
    public record ListRequest(String authToken) {}
    public record ListResult(Collection<GameData> games) {}
}
