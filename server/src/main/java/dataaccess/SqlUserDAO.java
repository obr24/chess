package dataaccess;

import model.UserData;
import service.ServiceException;

public class SqlUserDAO implements UserDAO {
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(id),
              INDEX(username),
              INDEX(password),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    public SqlUserDAO() throws ServiceException {
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void reset() throws DataAccessException {

    }

    @Override
    public boolean validUser(String username, String password) {
        return false;
    }
}
