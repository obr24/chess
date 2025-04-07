package client;

import com.google.gson.Gson;
import exception.ResponseException;
import model.RequestsAndResults;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade() {
        serverUrl = "http://127.0.0.1:8080"; // TO DO: change default serverurl value
    }
    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RequestsAndResults.RegisterResult register(RequestsAndResults.RegisterRequest request) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", null, path, request, RequestsAndResults.RegisterResult.class);
    }

    public RequestsAndResults.LoginResult login(RequestsAndResults.LoginRequest request) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", null, path, request, RequestsAndResults.LoginResult.class);
    }

    public RequestsAndResults.ClearResult clear(RequestsAndResults.ClearRequest request) throws ResponseException {
        var path = "/db";
        return this.makeRequest("DELETE", null, path, request, RequestsAndResults.ClearResult.class);
    }

    RequestsAndResults.LogoutResult logout(RequestsAndResults.LogoutRequest request) throws ResponseException {
        var path = "/session";
        return this.makeRequest("DELETE", request.authToken(), path, request, RequestsAndResults.LogoutResult.class);
    }

    RequestsAndResults.CreateResult create(RequestsAndResults.CreateRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", request.authToken(), path, request, RequestsAndResults.CreateResult.class);
    }

    RequestsAndResults.JoinResult join(RequestsAndResults.JoinRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("PUT", request.authToken(), path, request, RequestsAndResults.JoinResult.class);
    }

    RequestsAndResults.ListResult list(RequestsAndResults.ListRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", request.authToken(), path, null, RequestsAndResults.ListResult.class);
    }

    private <T> T makeRequest(String method, String authToken, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.addRequestProperty("Authorization", authToken);
            }

            if (request != null) {
                writeBody(request, http);
            }
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
