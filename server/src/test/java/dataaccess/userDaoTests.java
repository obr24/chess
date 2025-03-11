package dataaccess;

import dataaccess.MemoryUserDAO;
import dataaccess.SqlUserDAO;
import dataaccess.UserDAO;
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
}
