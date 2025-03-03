package service;

import dataaccess.*;
import model.AuthData;
import model.RequestsAndResults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.ServiceException;

import java.util.Objects;

import static service.UserService.logout;
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

    @Test
    @Order(2)
    @DisplayName("register result")
    public void registerResultTest() {
        RequestsAndResults.RegisterResult registerResult = null;
        try {
            registerResult = register(memoryUserDAO, memoryAuthDAO,
                    new RequestsAndResults.RegisterRequest("u1", "p1", "e1"));
        }
        catch (ServiceException e) {
            Assertions.assertNull(e);
        }
        Assertions.assertNotNull(registerResult);
    }

    @Test
    @Order(3)
    @DisplayName("logout")
    public void logoutTest() {
        RequestsAndResults.LogoutResult logoutResult = null;
        try {
            AuthData authData = new AuthData("test auth token", "u");
            try {
                memoryAuthDAO.createAuth(authData);
            }
            catch (DataAccessException e) {
                Assertions.assertNull(e);
            }
            logoutResult = logout(memoryUserDAO, memoryAuthDAO,
                    new RequestsAndResults.LogoutRequest("test auth token"));
        }
        catch (ServiceException e) {
            Assertions.assertNull(e);
        }
        Assertions.assertNotNull(logoutResult);
    }

    @Test
    @Order(4)
    @DisplayName("reset")
    public void restTest() {
        try {
            memoryAuthDAO.reset();
            memoryUserDAO.reset();
            memoryGamesDAO.reset();
        } catch (DataAccessException e) {
            Assertions.assertNull(e);
        }
        Assertions.assertNotNull(memoryAuthDAO);
        Assertions.assertNotNull(memoryUserDAO);
        Assertions.assertNotNull(memoryGamesDAO);

    }

    @Test
    @Order(5)
    @DisplayName("registerBadRequest")
    public void registerBadRequest() {
        try {
            UserService.register(memoryUserDAO, memoryAuthDAO,
                    new RequestsAndResults.RegisterRequest("", "", ""));
        } catch (ServiceException e) {
            Assertions.assertNotNull(e);
        }
    }

    @Test
    @Order(6)
    @DisplayName("loginBadRequest")
    public void loginBadRequest() {
        try {
            UserService.login(memoryUserDAO, memoryAuthDAO,
                    new RequestsAndResults.LoginRequest("", ""));
        }
        catch(ServiceException e) {
            Assertions.assertNotNull(e);
        }
    }
}
