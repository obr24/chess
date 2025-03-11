package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class AuthDaoTests {
    @Test
    @Order(1)
    @DisplayName("New auth dao")
    public void createAuthDAOTest() {
        try {
            AuthDAO authDAO = new SqlAuthDAO();
        } catch (Exception e) {
            Assertions.assertNull(e);
        }
    }

    @Test
    @Order(2)
    @DisplayName("create auth data")
    public void createAuthDataTest() {
        try {
            AuthDAO authDAO = new SqlAuthDAO();
            authDAO.createAuth(new AuthData("authtoken", "username"));
        } catch (Exception e) {
            Assertions.fail("failed w exception");
        }
    }

    @Test
    @Order(3)
    @DisplayName("create auth data fail")
    public void createAuthDataFailTest() {
        try {
            AuthDAO authDAO = new SqlAuthDAO();
            authDAO.createAuth(new AuthData("", ""));
        } catch (Exception e) {
            Assertions.assertNull(e);
        }
    }

    @Test
    @Order(4)
    @DisplayName("remove auth data")
    public void removeAuthDataTest() {
        try {
            AuthDAO authDAO = new SqlAuthDAO();
            authDAO.createAuth(new AuthData("authtoken", "username"));
            authDAO.removeAuthToken("authtoken");
        } catch (Exception e) {
            Assertions.fail("failed w exception");
        }
    }

    @Test
    @Order(5)
    @DisplayName("reset")
    public void resetAuthDataTest() {
        try {
            AuthDAO authDAO = new SqlAuthDAO();
            authDAO.createAuth(new AuthData("authtoken", "username"));
            authDAO.reset();
        } catch (Exception e) {
            Assertions.fail("failed w exception");
        }
    }
}