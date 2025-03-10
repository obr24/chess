package dataaccess;

import chess.ChessGame;
import chess.PieceMovesCalculator;
import com.google.gson.*;
import model.GameData;
import model.RequestsAndResults;
import model.UserData;
import service.ServiceException;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class SqlGamesDAO implements GamesDAO {
    Gson gson = new Gson();
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256),
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

        String json = gson.toJson(newGame);
        int gameID = DatabaseManager.executeUpdate(statement, null, null, gameName, json);
        return new RequestsAndResults.CreateResult(gameID);
    }

    private GameData readGameData(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("gameID");
        String gameName = rs.getString("gameName");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameJson = rs.getString("game");

        System.out.printf("\nGSON TO STRING (read) %s", gson.toString());
        ChessGame gameObject = new Gson().fromJson(gameJson, ChessGame.class);

        return new GameData(gameID, whiteUsername, blackUsername, gameName, gameObject);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID=?";
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        return readGameData(resultSet);
                    }
                }
            }
        }
        catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        throw new DataAccessException("Game doesn't exist or something");
    }

    @Override
    public void updateGame(ChessGame.TeamColor playerColor, int gameID, String username) throws DataAccessException {
        GameData gameData = getGame(gameID);
        GameData newGameData;
        if (playerColor == ChessGame.TeamColor.WHITE /*&& gameData.whiteUsername()*/) {
            newGameData = new GameData(gameID, username, gameData.blackUsername(), gameData.gameName(), gameData.game());
        } else if (playerColor == ChessGame.TeamColor.BLACK /* todo add test for blank black uname */) {
            newGameData = new GameData(gameID, gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
        } else {
            throw new DataAccessException("incorrect color submitted");
        }
        var statement = "UPDATE games SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?";

        DatabaseManager.executeUpdate(statement, newGameData.whiteUsername(), newGameData.blackUsername(),
                newGameData.gameName(), gson.toJson(newGameData.game()), gameID);
    }

    @Override
    public void reset() throws DataAccessException {
        String statement = "TRUNCATE games";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public Collection<GameData> getGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        games.add(readGameData(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return games;
    }
}
