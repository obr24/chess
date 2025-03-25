package client;

import exception.ResponseException;
import model.RequestsAndResults;
import org.junit.jupiter.api.*;
import server.Server;

import static chess.ChessGame.TeamColor.WHITE;
import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(String.format("http://127.0.0.1:%s", port));
    }

    @AfterEach
    public void deleteDB() {
        System.out.println("Clearing out the db for the next test");
        try {
            facade.clear(new RequestsAndResults.ClearRequest());
        } catch (ResponseException e) {
            System.out.println("Error: DB clear failed");
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void deleteDBTest() {
        try {
            facade.clear(new RequestsAndResults.ClearRequest());
            assertTrue(true);
        } catch (ResponseException e) {
            assertNotNull(e);
        }
    }

    @Test
    void registerTest() throws Exception {
        var testRequest = new RequestsAndResults.RegisterRequest("player1", "password", "p1@email.com");
        RequestsAndResults.RegisterResult authData = facade.register(testRequest);
        var authToken = authData.authToken();
        assertTrue(authToken.length() > 10);
    }

    @Test
    void badRegisterTest() throws Exception {
        var testRequest = new RequestsAndResults.RegisterRequest("player1", "password", "p1@email.com");
        RequestsAndResults.RegisterResult authData = facade.register(testRequest);
        try {
            RequestsAndResults.RegisterResult authData1 = facade.register(testRequest);
            fail("register wasn't rejected");
        } catch (ResponseException e) {
            assertNotNull(e);
        }
        var authToken = authData.authToken();
        assertTrue(authToken.length() > 10);
    }

    @Test
    void loginTest() throws Exception {
        var registerTestRequest = new RequestsAndResults.RegisterRequest("player1", "password", "p1@email.com");
        RequestsAndResults.RegisterResult registerAuthData = facade.register(registerTestRequest);

        var testRequest = new RequestsAndResults.LoginRequest("player1", "password");
        RequestsAndResults.LoginResult authData = facade.login(testRequest);
        var authToken = authData.authToken();
        System.out.println(authToken);
        assertTrue(authToken.length() > 10);
    }

    @Test
    void badLoginTest() throws Exception {
        var registerTestRequest = new RequestsAndResults.RegisterRequest("player1", "password", "p1@email.com");
        RequestsAndResults.RegisterResult registerAuthData = facade.register(registerTestRequest);
        var testRequest = new RequestsAndResults.LoginRequest("player2", "password");

        try {
            RequestsAndResults.LoginResult authData = facade.login(testRequest);
            fail("login was accepted");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test
    void logoutTest() throws Exception {
        var registerTestRequest = new RequestsAndResults.RegisterRequest("player1", "password", "p1@email.com");
        RequestsAndResults.RegisterResult registerAuthData = facade.register(registerTestRequest);

        var authToken = registerAuthData.authToken();

        var logoutTestRequest = new RequestsAndResults.LogoutRequest(authToken);
        try {
            RequestsAndResults.LogoutResult result = facade.logout(logoutTestRequest);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void badLogoutTest() throws Exception {
        var registerTestRequest = new RequestsAndResults.RegisterRequest("player1", "password", "p1@email.com");
        RequestsAndResults.RegisterResult registerAuthData = facade.register(registerTestRequest);

        var authToken = "fsadfsdadafs";

        var logoutTestRequest = new RequestsAndResults.LogoutRequest(authToken);
        try {
            RequestsAndResults.LogoutResult result = facade.logout(logoutTestRequest);
            fail("succeeded when it shouldn't have");
        } catch (Exception e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    void createGameTest() throws Exception {
        var registerTestRequest = new RequestsAndResults.RegisterRequest("player1", "password", "p1@email.com");
        RequestsAndResults.RegisterResult registerAuthData = facade.register(registerTestRequest);

        var authToken = registerAuthData.authToken();

        var createGameRequest = new RequestsAndResults.CreateRequest(authToken, "game1");
        try {
            var result = facade.create(createGameRequest);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void badCreateGameTest() throws Exception {
        var authToken = "blah blah";

        var createGameRequest = new RequestsAndResults.CreateRequest(authToken, "game1");
        try {
            var result = facade.create(createGameRequest);
            fail("should have failed with bad auth");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void joinGameTest() throws Exception {
        var registerTestRequest = new RequestsAndResults.RegisterRequest("player1", "password", "p1@email.com");
        RequestsAndResults.RegisterResult registerAuthData = facade.register(registerTestRequest);

        var authToken = registerAuthData.authToken();

        var createGameRequest = new RequestsAndResults.CreateRequest(authToken, "game1");

        RequestsAndResults.CreateResult result = new RequestsAndResults.CreateResult(0);
        try {
            result = facade.create(createGameRequest);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        var joinGameRequest = new RequestsAndResults.JoinRequest(authToken, "WHITE", result.gameID());

        try {
            var joinResult = facade.join(joinGameRequest);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Test
    void badJoinGameTest() throws Exception {
        var registerTestRequest = new RequestsAndResults.RegisterRequest("player1", "password", "p1@email.com");
        RequestsAndResults.RegisterResult registerAuthData = facade.register(registerTestRequest);

        var authToken = registerAuthData.authToken();

        var createGameRequest = new RequestsAndResults.CreateRequest(authToken, "game1");

        RequestsAndResults.CreateResult result = new RequestsAndResults.CreateResult(0);
        try {
            result = facade.create(createGameRequest);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        var joinGameRequest = new RequestsAndResults.JoinRequest(authToken, "WE", result.gameID());

        try {
            var joinResult = facade.join(joinGameRequest);
            fail("shouldve failed here for bad player color");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test
    void listGamesTest() throws Exception {
        var registerTestRequest = new RequestsAndResults.RegisterRequest("player1", "password", "p1@email.com");
        RequestsAndResults.RegisterResult registerAuthData = facade.register(registerTestRequest);

        var authToken = registerAuthData.authToken();

        var listGameRequest = new RequestsAndResults.ListRequest(authToken);

        try {
            var listResult = facade.list(listGameRequest);
        } catch (Exception e) {
            fail(String.format("bad here: %s", e.getMessage()));
        }
    }


    @Test
    void badListGamesTest() throws Exception {
        var registerTestRequest = new RequestsAndResults.RegisterRequest("player1", "password", "p1@email.com");
        RequestsAndResults.RegisterResult registerAuthData = facade.register(registerTestRequest);

        var authToken = "sadfsafdfsda";
        var listGameRequest = new RequestsAndResults.ListRequest(authToken);

        try {
            var listResult = facade.list(listGameRequest);
            fail(String.format("should have failed here"));
        } catch (Exception e) {
            assertNotNull(e);
        }
    }
}
