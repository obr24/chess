package dataaccess;

import dataaccess.MemoryUserDAO;
import dataaccess.SqlUserDAO;
import dataaccess.UserDAO;
import dataaccess.GamesDAO;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class userDaoTests {
    UserDAO memoryUserDAO = new MemoryUserDAO();

    @Test
    @Order(1)
    @DisplayName("New user dao")
    public void createUserDAOTest() {
        try {
            UserDAO userDAO = new SqlUserDAO();
        } catch (Exception e) {
            Assertions.assertNull(e);
        }
    }

    @Test
    @Order(2)
    @DisplayName("New game dao")
    public void createGameDAOTest() {
        try {
            GamesDAO gamesDAO = new SqlGamesDAO();
        } catch (Exception e) {
            Assertions.assertNull(e);
        }
    }

    @Test
    @Order(3)
    @DisplayName("New auth dao")
    public void createAuthDAOTest() {
        try {
            AuthDAO authDAO = new SqlAuthDAO();
        } catch (Exception e) {
            Assertions.assertNull(e);
        }
    }

    @Test
    @Order(4)
    @DisplayName("Create user successfully")
    public void createUserSuccessTest() {
        try {
            UserDAO userDAO = new SqlUserDAO();
            UserData testUser = new UserData("testUser", "securePassword123", "test@example.com");

            userDAO.createUser(testUser);
        } catch (Exception e) {
            Assertions.assertNull(e);
        }
    }

    @Test
    @Order(5)
    @DisplayName("Create user with duplicate username")
    public void createUserDuplicateTest() {
        try {
            UserDAO userDAO = new SqlUserDAO();
            UserData testUser = new UserData("", "", "");

            userDAO.createUser(testUser); // First insert (should succeed)
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

}
