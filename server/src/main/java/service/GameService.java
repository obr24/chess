package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GamesDAO;
import dataaccess.UserDAO;
import model.GameData;
import model.RequestsAndResults;
import model.UserData;

import java.util.Objects;

public class GameService {
    private static boolean validAuthToken(AuthDAO authDAO, String authToken) {
        try {
            authDAO.getAuth(authToken);
            return true;
        }
        catch (DataAccessException e) {
            return false;
        }
    }

    public static RequestsAndResults.CreateResult createGame(GamesDAO gamesDAO, AuthDAO authDAO, String authToken,
                                                             String gameName) throws ServiceException{
        if (!validAuthToken(authDAO, authToken)) {
            throw new ServiceException("Error: unauthorized", 401);
        }
        if (Objects.equals(gameName, "") || Objects.isNull(gameName)) {
            throw new ServiceException("Error: bad request", 400);
        }
        try {
            return new RequestsAndResults.CreateResult(gamesDAO.createGame(gameName).gameID());
        } catch (DataAccessException e) {
            throw new ServiceException(e.getMessage(), 500);
        }
    }

    public static RequestsAndResults.JoinResult joinGame(UserDAO userDAO, GamesDAO gamesDAO, AuthDAO authDAO, String authToken,
                                                         String playerColor, int gameID) throws ServiceException {
        UserData currUser;
        try {
            currUser = userDAO.getUser(authDAO.getAuth(authToken).username());
        } catch (DataAccessException e) {
            throw new ServiceException("Error: unauthorized", 401);
        }
        GameData currGame;
        try {
            currGame = gamesDAO.getGame(gameID);
        } catch (DataAccessException e) {
            throw new ServiceException("Error: bad request", 400);
        }
        ChessGame.TeamColor wantedColor;
        if (Objects.equals(playerColor, "WHITE")) {
            wantedColor = ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(playerColor, "BLACK")) {
            wantedColor = ChessGame.TeamColor.BLACK;
        } else {
            throw new ServiceException("Error: bad request", 400);
        }

        if ((wantedColor == ChessGame.TeamColor.WHITE && !Objects.isNull(currGame.whiteUsername()))
            || (wantedColor == ChessGame.TeamColor.BLACK && !Objects.isNull(currGame.blackUsername()))) {
            throw new ServiceException("Error: already taken", 403);
        } else {
            try {
                gamesDAO.updateGame(wantedColor, gameID, currUser.username());
            } catch (DataAccessException e) {
                throw new ServiceException("some error in GameService.java", 500);
            }
        }
        return new RequestsAndResults.JoinResult(gameID);
    }

    public static RequestsAndResults.ListResult listGames(UserDAO userDAO, GamesDAO gamesDAO, AuthDAO authDAO,
                                                          String authToken) throws ServiceException {
        UserData currUser;
        try {
            currUser = userDAO.getUser(authDAO.getAuth(authToken).username());
        } catch (DataAccessException e) {
            throw new ServiceException("Error: unauthorized", 401);
        }

        try {
            return new RequestsAndResults.ListResult(gamesDAO.getGames());
        } catch (DataAccessException e) {
            throw new ServiceException("Error: somewhere in gameServer.java", 500);
        }
    }

    public static void reset(GamesDAO gamesDAO) throws ServiceException {
        try {
            gamesDAO.reset();
        } catch (DataAccessException e) {
            throw new ServiceException("Error: somewhere else in gameService.java", 500);
        }
    }
}
