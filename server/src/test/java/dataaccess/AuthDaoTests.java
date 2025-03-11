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
}