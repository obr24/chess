package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private Collection<AuthData> auths = new ArrayList<>();

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        AuthData newAuth = new AuthData(authData.authToken(), authData.username());
        this.auths.add(newAuth);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        throw new DataAccessException("implement it");
    }
}
