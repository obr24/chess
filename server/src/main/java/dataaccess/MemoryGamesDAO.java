package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.RequestsAndResults;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGamesDAO implements GamesDAO {
    private Collection<GameData> games = new ArrayList<>();
    private int incrementor = 1;

    public Collection<GameData> getGames() throws DataAccessException {
        return games;
    }

    @Override
    public void setGame(int gameID) throws DataAccessException {
        GameData curGame = getGame(gameID);
        games.remove(curGame);
        games.add(game);
    }

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
        GameData curGame = getGame(gameID);
        String whiteUsername = curGame.whiteUsername();
        String blackUsername = curGame.blackUsername();
        String gameName = curGame.gameName();
        ChessGame game = curGame.game();

        if (playerColor == ChessGame.TeamColor.WHITE) {
            whiteUsername = username;
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            blackUsername = username;
        } else {
            throw new DataAccessException("Username didn't match up...");
        }
        games.remove(curGame);
        games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
    }

    @Override
    public void reset() throws DataAccessException {
        games.clear();
    }
}
