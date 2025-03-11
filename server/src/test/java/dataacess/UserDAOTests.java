package dataacess;

import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.RequestsAndResults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.ServiceException;
import service.UserService;

public class UserDAOTests {

    @Test
    @Order(1)
    @DisplayName("create db")
    public void createDBTest() {
        try {
            UserDAO memoryUserDAO = new MemoryUserDAO();
        } catch (Exception e) {
            Assertions.assertNull(e);
        }
    }
}
