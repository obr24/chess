package dataacess;

import dataaccess.*;
import model.RequestsAndResults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.ServiceException;
import service.UserService;

public class UUUUserDAOTests {

    @Test
    @Order(1)
    @DisplayName("create user db")
    public void createDBTest() {
        try {
            UserDAO memoryUserDAO = new SqlUserDAO();
        } catch (Exception e) {
            Assertions.assertNull(e);
        }
    }

    @Test
    @Order(2)
    @DisplayName("create auth db")
    public void createAuthDBTest() {
        try {
            AuthDAO memoryAuthDAO = new SqlAuthDAO();
        } catch (Exception e) {
            Assertions.assertNull(e);
        }
    }

    @Test
    @Order(3)
    @DisplayName("create game db")
    public void createGameDBTest() {
        try {
            GamesDAO memoryGamesDAO = new SqlGamesDAO();
        } catch (Exception e) {
            Assertions.assertNull(e);
        }
    }
}
