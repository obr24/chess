package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.RequestsAndResults.*;
import service.ServiceException;
import spark.*;

public class Server {

    UserDAO memoryUserDAO = new MemoryUserDAO();
    //AuthDAO authDAO = new MemoryAuthDAO();
    GamesDAO memoryGamesDAO = new MemoryGamesDAO();

    AuthDAO authDAO = null;

    public int run(int desiredPort) {
        try {
            authDAO = new SqlAuthDAO();
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::addUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/db", this::clearDB);
        Spark.delete("/session", this::logoutUser);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.get("/game", this::listGames);


        Spark.exception(ServiceException.class, (exception, request, response) -> {
            response.status(exception.getResponseCode());
            response.body(new Gson().toJson(exception.getResponse()));
        });

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object addUser(Request req, Response res) throws ServiceException {
        var registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        var registerResult = service.UserService.register(memoryUserDAO, authDAO, registerRequest);
        return new Gson().toJson(registerResult);
    }

    private Object clearDB(Request request, Response response) throws ServiceException {
        service.UserService.reset(memoryUserDAO, authDAO);
        service.GameService.reset(memoryGamesDAO);
        return new Gson().toJson(new ClearResult());
    }

    private Object loginUser(Request request, Response response) throws ServiceException {
        var loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);
        var loginResult = service.UserService.login(memoryUserDAO, authDAO, loginRequest);
        return new Gson().toJson(loginResult);
    }

    private Object logoutUser(Request request, Response response) throws ServiceException {
        String authToken = request.headers("authorization");
        var logoutResult = service.UserService.logout(memoryUserDAO, authDAO, new LogoutRequest(authToken));
        return new Gson().toJson(logoutResult);
    }

    private Object createGame(Request request, Response response) throws ServiceException {
        String authToken = request.headers("authorization");
        String gameName = new Gson().fromJson(request.body(), CreateRequest.class).gameName();
        CreateResult createResult = service.GameService.createGame(memoryGamesDAO, authDAO, authToken, gameName);
        return new Gson().toJson(createResult);
    }

    private Object joinGame(Request request, Response response) throws ServiceException {
        String authToken = request.headers("authorization");
        String playerColor = new Gson().fromJson(request.body(), JoinRequest.class).playerColor();
        int gameID = new Gson().fromJson(request.body(), JoinRequest.class).gameID();
        JoinResult joinResult = service.GameService.joinGame(memoryUserDAO, memoryGamesDAO, authDAO, authToken,
                                playerColor, gameID);
        return new Gson().toJson(joinResult);
    }

    private Object listGames(Request request, Response response) throws ServiceException {
        String authToken = request.headers("authorization");
        ListResult listResult = service.GameService.listGames(memoryUserDAO, memoryGamesDAO, authDAO, authToken);
        return new Gson().toJson(listResult);
    }
}
