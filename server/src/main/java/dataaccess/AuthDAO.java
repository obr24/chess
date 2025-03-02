package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void removeAuthToken(String authToken) throws DataAccessException;
    void reset() throws DataAccessException;
}



