package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import handler.Handler;
import handler.JoinGameInput;
import model.GameData;
import model.UserData;
import exceptions.BadServiceRequest;
import service.ErrorMessage;
import exceptions.UnauthorizedAccessError;
import exceptions.UserAlreadyTakenError;
import spark.*;

import java.util.Map;

public class Server {
    private final Handler handler = new Handler();
    private final Gson serializer = new Gson();

    public Server() {
        //this is the default constructor, I need to set this equal to memory implementations

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::login);
        Spark.delete("/db", this::clearAllDB);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.get("/game", this::listGames);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private String clearAllDB(Request req, Response res) {
        handler.clearAllDB();
        res.status(200);
        return "";
    }

    private String listGames(Request req, Response res) {
        String authToken = req.headers("authorization");
        try {
            var games = handler.listGames(authToken);
            if (games == null) {
                res.status(200);
                return "";
            }
            return serializer.toJson(Map.of("games", games));
        } catch (UnauthorizedAccessError e) {
            res.status(401);
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return serializer.toJson(errorMessage);
        } catch (Exception e) {
            res.status(501);
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return serializer.toJson(errorMessage);
        }
    }

    private String joinGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        JoinGameInput joinGameInput = serializer.fromJson(req.body(), JoinGameInput.class);
        try {
            handler.updateGamePlayer(authToken, joinGameInput.playerColor(), joinGameInput.gameID());
            return "";
        } catch (BadServiceRequest e) {
            res.status(400);
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return serializer.toJson(errorMessage);
        } catch (UnauthorizedAccessError e) {
            res.status(401);
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return serializer.toJson(errorMessage);
        } catch (UserAlreadyTakenError e) {
            res.status(403);
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return serializer.toJson(errorMessage);
        }
    }

    private String createGame(Request req, Response res) {
        var authToken = req.headers("authorization");
        var gameName = serializer.fromJson(req.body(), GameData.class);
        //this game should only have a gameName attached
        try {
            var result = handler.createGame(authToken, gameName.gameName());
            GameData responseData = new GameData(result, null, null, null, null);
            return serializer.toJson(responseData);
        } catch (UnauthorizedAccessError e) {
            res.status(401);
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return serializer.toJson(errorMessage);
        }
    }

    private String registerUser(Request req, Response res) {
        var newUser = serializer.fromJson(req.body(), UserData.class);
        try {
            var result = handler.registerUser(newUser);
            return serializer.toJson(result);
        } catch (UserAlreadyTakenError e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            res.status(403);
            return serializer.toJson(errorMessage);
        } catch (BadServiceRequest e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            res.status(400);
            return serializer.toJson(errorMessage);
        }

    }

    private String login(Request req, Response res) {
        var loginUser = serializer.fromJson(req.body(), UserData.class);
        try {
            var result = handler.login(loginUser);
            return serializer.toJson(result);
        } catch (UserAlreadyTakenError e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            res.status(403);
            return serializer.toJson(errorMessage);
        } catch (UnauthorizedAccessError e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            res.status(401);
            return serializer.toJson(errorMessage);
        }
    }

    public String logout(Request req, Response res) {
        var logoutAuthToken = (req.headers("authorization"));
        try {
            handler.logout(logoutAuthToken);
            return "";
        } catch (UnauthorizedAccessError e) {
            res.status(401);
            ErrorMessage errorMessage = new ErrorMessage("Error: unauthorized");
            return serializer.toJson(errorMessage);
        }
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
