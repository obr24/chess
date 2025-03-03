package service;

import dataaccess.*;
import model.RequestsAndResults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class GameServiceTests {
    UserDAO memoryUserDAO = new MemoryUserDAO();
    AuthDAO memoryAuthDAO = new MemoryAuthDAO();
    GamesDAO memoryGamesDAO = new MemoryGamesDAO();

    @Test
    @Order(1)
    @DisplayName("createGame")
    public void createGameTest() {
        String authToken = null;
        try {
            authToken = UserService.register(memoryUserDAO, memoryAuthDAO,
                    new RequestsAndResults.RegisterRequest(
                            "username", "password", "email")).authToken();
        } catch (Exception e) {
            Assertions.assertNull(e);
        }
        try {
            Assertions.assertNotNull(GameService.createGame(memoryGamesDAO, memoryAuthDAO, authToken, "gameName"));
        } catch (ServiceException e) {
            Assertions.assertNull(e);
        }
    }
}
