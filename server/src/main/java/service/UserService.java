package service;

import dataaccess.DataAccessException;
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

    private static AuthData CreateAuthDataForUser(String username) {
        String newAuthToken = UUID.randomUUID().toString();
        return new AuthData(newAuthToken, username);
    }

    public static RegisterResult register(UserDAO userDAO, RegisterRequest registerRequest) throws ServiceException { // TODO: definitely remove? only added static b/c intellij told me to
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
        AuthData newAuthData = CreateAuthDataForUser(registerRequest.username());

        return new RegisterResult(newAuthData.username(), newAuthData.authToken());
    }

    private boolean UserValid(UserDAO userDAO, String username, String password) {
            return userDAO.validUser(username, password);
    }

    public static LoginResult login(UserDAO userDAO, LoginRequest loginRequest) throws ServiceException {
        if (userDAO.validUser(loginRequest.username(), loginRequest.password())) {
            AuthData newAuthToken = CreateAuthDataForUser(loginRequest.username());
            return new LoginResult(loginRequest.username(), newAuthToken.authToken());
        } else {
            throw new ServiceException("Error: unauthorized", 401);
        }
    }

    public void logout(LogoutRequest logoutRequest) {}
}