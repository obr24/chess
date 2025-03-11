package dataaccess;

import dataaccess.SqlUserDAO;
import dataaccess.UserDAO;
import model.RequestsAndResults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.ServiceException;
import service.UserService;

public class DAOtests {
    UserDAO memoryUserDAO = new MemoryUserDAO();
    AuthDAO memoryAuthDAO = new MemoryAuthDAO();
    GamesDAO memoryGamesDAO = new MemoryGamesDAO();

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
