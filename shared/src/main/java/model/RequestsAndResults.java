package model;

public class RequestsAndResults {
    public record RegisterRequest(String username, String password, String email) {}
    public record RegisterResult(String username, String authToken) {}
    public record LoginRequest(
            String username,
            String password) {}
    public record LoginResult(
            String username,
            String authToken) {}
    public record LogoutRequest() {}

    public record ClearRequest() {}
    public record ClearResult() {}

    public record FailureResponse(int errorCode, String message) {}
}
