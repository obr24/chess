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

    @Test
    @Order(2)
    @DisplayName("createGame- bad auth request")
    public void createGameBadAuthTest() {
        String authToken = null;
        try {
            authToken = UserService.register(memoryUserDAO, memoryAuthDAO,
                    new RequestsAndResults.RegisterRequest(
                            "username", "password", "email")).authToken();
        } catch (Exception e) {
            Assertions.assertNull(e);
        }
        try {
            Assertions.assertNull(GameService.createGame(memoryGamesDAO, memoryAuthDAO, "", "gameName"));
        } catch (ServiceException e) {
            Assertions.assertNotNull(e);
        }
    }

    @Test
    @Order(3)
    @DisplayName("joinGame- good request")
    public void createGameGoodTest() {
        String authToken = null;
        int gameID = 0;
        try {
            authToken = UserService.register(memoryUserDAO, memoryAuthDAO,
                    new RequestsAndResults.RegisterRequest(
                            "username1", "password1", "email1")).authToken();
        } catch (Exception e) {
            Assertions.assertNull(e);
        } try {
            gameID = GameService.createGame(memoryGamesDAO, memoryAuthDAO, "", "gameName1").gameID();
            Assertions.assertNotEquals(0, gameID);
        } catch (ServiceException e) {
            Assertions.assertNotNull(e);
        } try {
            GameService.joinGame(memoryUserDAO, memoryGamesDAO, memoryAuthDAO, authToken, "WHITE", gameID);
        } catch (ServiceException e) {
            Assertions.assertNotNull(e);
        }
    }

    @Test
    @Order(4)
    @DisplayName("joinGame- good request1")
    public void createGameGoodTest1() {
        String authToken = null;
        int gameID = 0;
        try {
            authToken = UserService.register(memoryUserDAO, memoryAuthDAO,
                    new RequestsAndResults.RegisterRequest(
                            "username3", "password3", "email3")).authToken();
        } catch (Exception e) {
            Assertions.assertNull(e);
        }
        try {
            gameID = GameService.createGame(memoryGamesDAO, memoryAuthDAO, "", "gameName1").gameID();
            Assertions.assertNotEquals(0, gameID);
        } catch (ServiceException e) {
            Assertions.assertNotNull(e);
        }
        try {
            GameService.joinGame(memoryUserDAO, memoryGamesDAO, memoryAuthDAO, authToken, "WHITE", gameID);
        } catch (ServiceException e) {

        }
    }

    @Test
    @Order(5)
    @DisplayName("joinGame- good request2")
    public void createGameGoodTest2() {
        String authToken = null;
        int gameID = 0;
        try {
            authToken = UserService.register(memoryUserDAO, memoryAuthDAO,
                    new RequestsAndResults.RegisterRequest(
                            "username2", "password2", "email2")).authToken();
        } catch (Exception e) {
            Assertions.assertNull(e);
        }
        try {
            gameID = GameService.createGame(memoryGamesDAO, memoryAuthDAO, "", "gameName1").gameID();
            Assertions.assertNotEquals(0, gameID);
        } catch (ServiceException e) {
            Assertions.assertNotNull(e);
        }
        try {
            GameService.joinGame(memoryUserDAO, memoryGamesDAO, memoryAuthDAO, authToken, "WHITE", gameID);
        } catch (ServiceException e) {

        }
    }
}
