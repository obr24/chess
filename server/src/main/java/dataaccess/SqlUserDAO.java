package dataaccess;

import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.ServiceException;
import service.UserService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class SqlUserDAO implements UserDAO {
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
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
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        DatabaseManager.executeUpdate(statement, user.username(), getBcryptPassword(user.password()), user.email());
    }

    private UserData readUserData(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        return new UserData(username, password, email);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String statement = "SELECT username, password, email FROM users WHERE username=?";
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        return readUserData(resultSet);
                    }
                }
            }
        }
        catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        throw new DataAccessException("User doesn't exist or something");
    }

    @Override
    public void reset() throws DataAccessException {
        String statement = "TRUNCATE users";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public boolean validUser(String username, String password) {
        String actualPassword;
        try {
            actualPassword = getUser(username).password();
            if (actualPassword.isEmpty()) {
                return false;
            }
        } catch (DataAccessException e) {
            return false;
        }
        return BCrypt.checkpw(password, actualPassword);
//        return Objects.equals(actualPassword, getBcryptPassword(password));
    }

    private String getBcryptPassword(String password) {
        String bcryptPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return bcryptPassword;
    }
}
