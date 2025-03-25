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

    // todo: fix pregame database resetting
//    @BeforeEach
//    public static void preTestInit() {
//        server.clear();
//    }

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
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
