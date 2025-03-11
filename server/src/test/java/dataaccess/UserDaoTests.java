package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class UserDaoTests {

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

    @Test
    @Order(6)
    @DisplayName("Retrieve existing user")
    public void getUserSuccessTest() {
        try {
            UserDAO userDAO = new SqlUserDAO();
            UserData testUser = new UserData("existingUser", "securePassword123", "test@example.com");

            userDAO.createUser(testUser);

            UserData retrievedUser = userDAO.getUser("existingUser");

            Assertions.assertNotNull(retrievedUser);
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(7)
    @DisplayName("Retrieve non-existent user")
    public void getUserNotFoundTest() {
        try {
            UserDAO userDAO = new SqlUserDAO();

            // Attempt to get a user that does not exist
            UserData retrievedUser = userDAO.getUser("nonExistentUser");

            Assertions.assertNull(retrievedUser, "User should not exist in the database");

        } catch (DataAccessException e) {
            Assertions.assertNotNull(e);
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(8)
    @DisplayName("Get user that exists")
    public void getUserExistsTest() {
        try {
            UserDAO userDAO = new SqlUserDAO();
            UserData testUser = new UserData("existingUser", "securePassword123", "test@example.com");

            userDAO.createUser(testUser);

            UserData retrievedUser = userDAO.getUser("existingUser");

            Assertions.assertNotNull(retrievedUser.username());
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    @Order(9)
    @DisplayName("Get user that does not exist")
    public void getUserNotExistsTest() {
        try {
            UserDAO userDAO = new SqlUserDAO();

            UserData retrievedUser = userDAO.getUser("nonExistentUser");

            Assertions.fail("Expected DataAccessException was not thrown");
        } catch (DataAccessException e) {
            Assertions.assertNotNull(e, "Exception should be thrown when user does not exist");
            Assertions.assertTrue(e.getMessage().contains("User doesn't exist"), "Error message should indicate missing user");
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

}
