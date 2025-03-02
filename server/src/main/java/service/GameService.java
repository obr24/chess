package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GamesDAO;

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
    public static int createGame(GamesDAO gamesDAO, AuthDAO authDAO, String authToken, String gameName) throws ServiceException{
        if (!validAuthToken(authDAO, authToken)) {
            throw new ServiceException("Error: unauthorized", 401);
        }
        if (Objects.equals(gameName, "") || Objects.isNull(gameName)) {
            throw new ServiceException("Error: bad request", 400);
        }
        try {
            return gamesDAO.createGame(gameName).gameID();
        } catch (DataAccessException e) {
            throw new ServiceException(e.getMessage(), 500);
        }
    }
}
