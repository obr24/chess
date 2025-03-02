package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.RequestsAndResults.*;
import model.UserData;

import java.util.Collection;
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

        String newAuthToken = UUID.randomUUID().toString();

        AuthData newAuthData = new AuthData(newAuthToken, newUser.username());

        RegisterResult newRegisterResult = new RegisterResult(newAuthData.username(), newAuthData.authToken());
        return newRegisterResult;
    }

    public LoginResult login(LoginRequest loginRequest) {
        return null;
    }
    public void logout(LogoutRequest logoutRequest) {}
}