package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
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
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    public SqlGamesDAO() throws ServiceException {
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public RequestsAndResults.CreateResult createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        GameData newGame = new GameData(null, null, null, gameName, new ChessGame());
        String json = new Gson().toJson(newGame);
        DatabaseManager.executeUpdate(gameName, null, null, json)

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
        String statement = "TRUNCATE games";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public Collection<GameData> getGames() throws DataAccessException {
        return List.of();
    }
}
