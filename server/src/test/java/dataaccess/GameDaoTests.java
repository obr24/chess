package dataaccess;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GamesDAO;
import dataaccess.SqlAuthDAO;
import dataaccess.SqlGamesDAO;
import model.AuthData;
import model.GameData;
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

    @Test
    @Order(2)
    @DisplayName("reset game")
    public void resetGameDataTest() {
        try {
            GamesDAO gamesDAO = new SqlGamesDAO();
            gamesDAO.reset();
        } catch (Exception e) {
            Assertions.fail("failed w exception");
        }
    }
}
