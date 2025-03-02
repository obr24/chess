package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.RequestsAndResults;
import model.RequestsAndResults.*;
import service.ServiceException;
import service.UserService;
import spark.*;

import java.lang.module.ResolutionException;

public class Server {

    UserDAO memoryUserDAO = new MemoryUserDAO();
    AuthDAO memoryAuthDAO = new MemoryAuthDAO();
    GamesDAO memoryGamesDAO = new MemoryGamesDAO();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::addUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/db", this::clearDB); // TODO: u still need to add delete AuthDAO and GameDAO!
        Spark.delete("/session", this::logoutUser);
        Spark.post("/game", this::createGame);


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
//
//    private Object addPet(Request req, Response res) throws ResponseException {
//        var pet = new Gson().fromJson(req.body(), Pet.class);
//        pet = service.addPet(pet);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
//        return new Gson().toJson(pet);
//    }

    private Object addUser(Request req, Response res) throws ServiceException {
        var registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        var registerResult = service.UserService.register(memoryUserDAO, memoryAuthDAO, registerRequest);
        return new Gson().toJson(registerResult);
    }

    private Object clearDB(Request request, Response response) throws ServiceException {
        service.UserService.Reset(memoryUserDAO);
        return new Gson().toJson(new ClearResult());
    }

    private Object loginUser(Request request, Response response) throws ServiceException {
        var loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);
        var loginResult = service.UserService.login(memoryUserDAO, memoryAuthDAO, loginRequest);
        return new Gson().toJson(loginResult);
    }

    private Object logoutUser(Request request, Response response) throws ServiceException {
        String authToken = request.headers("authorization");
        var logoutResult = service.UserService.logout(memoryUserDAO, memoryAuthDAO, new LogoutRequest(authToken));
        return new Gson().toJson(logoutResult);
    }

    private Object createGame(Request request, Response response) throws ServiceException {
        String authToken = request.headers("authorization");
        String gameName = new Gson().fromJson(request.body(), CreateRequest.class).gameName();
        CreateResult createResult = service.GameService.createGame(memoryGamesDAO, memoryAuthDAO, authToken, gameName);
        return new Gson().toJson(createResult);
    }

}
