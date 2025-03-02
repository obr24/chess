package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.RequestsAndResults;
import model.RequestsAndResults.*;
import service.ServiceException;
import service.UserService;
import spark.*;

import java.lang.module.ResolutionException;

public class Server {

    UserDAO memoryUserDAO = new MemoryUserDAO();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::addUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/db", this::clearDB); // TODO: u still need to add delete AuthDAO and GameDAO!


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
        var registerResult = service.UserService.register(memoryUserDAO, registerRequest);
        return new Gson().toJson(registerResult);
    }

    private Object clearDB(Request request, Response response) throws ServiceException {
        service.UserService.Reset(memoryUserDAO);
        return new Gson().toJson(new ClearResult());
    }

    private Object loginUser(Request request, Response response) throws ServiceException {
        var loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);
        var loginResult = service.UserService.login(memoryUserDAO, loginRequest);
        return new Gson().toJson(loginResult);
    }
}
