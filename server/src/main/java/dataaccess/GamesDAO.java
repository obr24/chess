package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.RequestsAndResults.CreateResult;

import java.util.Collection;

public interface GamesDAO {
   CreateResult createGame(String gameName) throws DataAccessException;
   GameData getGame(int gameID) throws DataAccessException;
   void updateGame(ChessGame.TeamColor playerColor, int gameID, String username) throws DataAccessException;
   void reset() throws DataAccessException;
   Collection<GameData> getGames() throws DataAccessException;
}
