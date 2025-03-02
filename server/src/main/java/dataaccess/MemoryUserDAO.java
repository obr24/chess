package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO{

    private Collection<UserData> users = new ArrayList<UserData>();

    public void createUser(UserData userData) throws DataAccessException {
        UserData newUser = new UserData(userData.username(), userData.password(), userData.email());
//        throw(new DataAccessException("not implemented"));
        users.add(newUser);
    }

    public UserData getUser(String username) throws DataAccessException {
        for (UserData user : users) {
            if (Objects.equals(user.username(), username)) {  // TO-do: is this the right way to check if the usernames are the same
                return user;
            }
        }
        throw(new DataAccessException("User does not exist"));
    }

    public void reset() throws DataAccessException {
        users.clear();
    }

    public boolean validUser(String username, String password) {
        for (UserData user : users) {
            if (Objects.equals(user.username(), username) && Objects.equals(user.password(), password)) {
                return true;
            }
        }
        return false;
    }
}
