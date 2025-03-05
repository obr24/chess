package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import service.ServiceException;

import java.sql.SQLException;

public class SqlAuthDAO implements AuthDAO {

//    public MySqlDataAccess() throws ResponseException {
//        configureDatabase();
//    }

    public SqlAuthDAO() throws ServiceException {
        configureDatabase();
    }

//    private void configureDatabase() throws ResponseException {
//        DatabaseManager.createDatabase();
//        try (var conn = DatabaseManager.getConnection()) {
//            for (var statement : createStatements) {
//                try (var preparedStatement = conn.prepareStatement(statement)) {
//                    preparedStatement.executeUpdate();
//                }
//            }
//        } catch (SQLException ex) {
//            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
//        }
//    }

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
//        var statement = "INSERT INTO pet (name, type, json) VALUES (?, ?, ?)";
//        var json = new Gson().toJson(pet);
//        var id = executeUpdate(statement, pet.name(), pet.type(), json);
//        return new Pet(id, pet.name(), pet.type());
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        DatabaseManager.executeUpdate(statement, authData.authToken(), authData.username());
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
//        try (var conn = DatabaseManager.getConnection()) {
//            var statement = "SELECT id, json FROM pet WHERE id=?";
//            try (var ps = conn.prepareStatement(statement)) {
//                ps.setInt(1, id);
//                try (var rs = ps.executeQuery()) {
//                    if (rs.next()) {
//                        return readPet(rs);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
//        }
//        return null;
        return null;
    }

    @Override
    public void removeAuthToken(String authToken) throws DataAccessException {

    }

    @Override
    public void reset() throws DataAccessException {

    }
}
