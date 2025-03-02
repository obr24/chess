package service;

import dataaccess.*;
import model.RequestsAndResults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.ServiceException;

import java.util.Objects;

import static service.UserService.register;

public class UserServiceTests {

    UserDAO memoryUserDAO = new MemoryUserDAO();
    AuthDAO memoryAuthDAO = new MemoryAuthDAO();
    GamesDAO memoryGamesDAO = new MemoryGamesDAO();

    @Test
    @Order(1)
    @DisplayName("register")
    public void registerTest() {
        RequestsAndResults.RegisterResult registerResult = null;
        try {
            registerResult = register(memoryUserDAO, memoryAuthDAO,
                    new RequestsAndResults.RegisterRequest("u", "p", "e"));
        }
        catch (ServiceException e) {
            Assertions.assertNull(e);
        }
        Assertions.assertNotNull(registerResult);
    }
}
