package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.RequestsAndResults;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGamesDAO implements GamesDAO {
    private Collection<GameData> games = new ArrayList<>();
    private int incrementor = 1;

    private int getIdAndIncrement() {
        return incrementor++;
    }
    public RequestsAndResults.CreateResult createGame(String gameName) {
        GameData newGame = new GameData(getIdAndIncrement(), null, null, gameName, new ChessGame());
        games.add(newGame);
        return new RequestsAndResults.CreateResult(newGame.gameID());
    }

    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        throw new DataAccessException("game doesn't exist");
    }

    public void updateGame(ChessGame.TeamColor playerColor, int gameID, String username) throws DataAccessException {
        throw new DataAccessException("Not implemented!");
    }

    @Override
    public void reset() throws DataAccessException {
        games.clear();
    }
}
