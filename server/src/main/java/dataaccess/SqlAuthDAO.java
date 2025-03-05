package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import service.ServiceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlAuthDAO implements AuthDAO {
    public SqlAuthDAO() throws ServiceException {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `id` int NOT NULL AUTO_INCREMENT,
              `authToken` varchar(256) NOT NULL,
              `username` varchar(512) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(authToken),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws ServiceException {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new ServiceException(e.getMessage(), 500);
        }

        try (var connection = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (Exception e) {
            throw new ServiceException(String.format("Issue with database config: %s", e.getMessage()), 500);
        }

    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        DatabaseManager.executeUpdate(statement, authData.authToken(), authData.username());
    }

    private AuthData readAuthData(ResultSet rs) throws SQLException {
        String authToken = rs.getString("authToken");
        String username = rs.getString("username");
        return new AuthData(authToken, username);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        return readAuthData(resultSet);
                    }
                }
            }
        }
        catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void removeAuthToken(String authToken) throws DataAccessException {
        String statement = "DELETE FROM auth WHERE authToken=?";
        DatabaseManager.executeUpdate(statement, authToken);
    }

    @Override
    public void reset() throws DataAccessException {
        String statement = "TRUNCATE auth";
        DatabaseManager.executeUpdate(statement);
    }
}
