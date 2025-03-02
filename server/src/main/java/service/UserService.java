package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.RequestsAndResults.*;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    // !!! U cannot save any state here because everything is static !!!

    private static boolean userExists(UserDAO userDAO, String username) {
        try {
            userDAO.getUser(username);
        } catch (DataAccessException e) {
            return false;
        }
        return true;
    }

    private static boolean validRequest(RegisterRequest registerRequest) {
        boolean validUsername = !(Objects.equals(registerRequest.username(), "") ||
                                Objects.isNull(registerRequest.username()));
        boolean validPassword = !(Objects.equals(registerRequest.password(), "") ||
                                Objects.isNull(registerRequest.password()));
        boolean validEmail = !(Objects.equals(registerRequest.email(), "") ||
                             Objects.isNull(registerRequest.email()));
        return validUsername && validPassword && validEmail;
    }

    public static void Reset(UserDAO userDAO) throws ServiceException {
        try {
            userDAO.reset();
        } catch (DataAccessException e) {
            throw new ServiceException(e.getMessage(), 500); // TODO: test to see if works
        }
    }

    private static AuthData CreateAuthDataForUser(AuthDAO authDAO, String username) throws ServiceException {
        String newAuthToken = UUID.randomUUID().toString();
        AuthData newAuthData = new AuthData(newAuthToken, username);
        try {
            authDAO.createAuth(newAuthData);
        } catch (DataAccessException e) {
            throw new ServiceException("u screwed up when creating a new auth token in UserService.java", 500);
        }
        return new AuthData(newAuthToken, username);
    }

    public static RegisterResult register(UserDAO userDAO, AuthDAO authDAO, RegisterRequest registerRequest) throws ServiceException { // TODO: definitely remove? only added static b/c intellij told me to
        // check if
        if (!validRequest(registerRequest)) {
            throw new ServiceException("Error: bad request", 400);
        }

        if (userExists(userDAO, registerRequest.username())) {
            throw new ServiceException("Error: already taken", 403);
        }
        UserData newUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());

        try {
            userDAO.createUser(newUser);
        } catch (DataAccessException e) {
            throw new RuntimeException(e); // TODO: return error result
        }

        AuthData newAuthData = CreateAuthDataForUser(authDAO, registerRequest.username());

        return new RegisterResult(newAuthData.username(), newAuthData.authToken());
    }

    private boolean UserValid(UserDAO userDAO, String username, String password) {
            return userDAO.validUser(username, password);
    }

    public static LoginResult login(UserDAO userDAO, AuthDAO authDAO, LoginRequest loginRequest) throws ServiceException {
        if (userDAO.validUser(loginRequest.username(), loginRequest.password())) {
            AuthData newAuthToken = CreateAuthDataForUser(authDAO, loginRequest.username());
            return new LoginResult(loginRequest.username(), newAuthToken.authToken());
        } else {
            throw new ServiceException("Error: unauthorized", 401);
        }
    }

    private static void RemoveAuthToken(AuthDAO authDAO, String authToken) throws ServiceException {
        try {
            authDAO.removeAuthToken(authToken);
        }
        catch(DataAccessException e) {
            throw new ServiceException("Error: authtoken does not exist", 401); // todo add in error for unauthorized
        }
    }

    public static LogoutResult logout(UserDAO userDAO, AuthDAO authDAO, LogoutRequest logoutRequest) throws ServiceException {
        RemoveAuthToken(authDAO, logoutRequest.authToken());
        return new LogoutResult();
    }
}