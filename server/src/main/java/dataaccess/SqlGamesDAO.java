package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.RequestsAndResults;
import service.ServiceException;

import java.util.Collection;
import java.util.List;

public class SqlGamesDAO implements GamesDAO {
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(gameID),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(gameName),
              INDEX(game)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    public SqlGamesDAO() throws ServiceException {
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public RequestsAndResults.CreateResult createGame(String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(ChessGame.TeamColor playerColor, int gameID, String username) throws DataAccessException {

    }

    @Override
    public void reset() throws DataAccessException {

    }

    @Override
    public Collection<GameData> getGames() throws DataAccessException {
        return List.of();
    }
}
