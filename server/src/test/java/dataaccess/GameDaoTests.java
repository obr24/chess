package java.dataaccess;

import dataaccess.AuthDAO;
import dataaccess.GamesDAO;
import dataaccess.SqlAuthDAO;
import dataaccess.SqlGamesDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class GameDaoTests {
    @Test
    @Order(1)
    @DisplayName("New game dao")
    public void createGameDAOTest() {
        try {
            GamesDAO gamesDAO = new SqlGamesDAO();
        } catch (Exception e) {
            Assertions.assertNull(e);
        }
    }
}
