package server;

import com.google.gson.Gson;
import handler.Handler;
import model.UserData;
import service.ErrorMessage;
import service.UnauthorizedAccessError;
import service.UserAlreadyTakenError;
import spark.*;

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
//        Spark.post();
        Spark.post("/user", this::createUser);
        Spark.post("/session", this::login);
        Spark.delete("/db", this::clearAllDB);
        Spark.delete("/session", this::logout);

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


    private String createUser(Request req, Response res) {
        var newUser = serializer.fromJson(req.body(), UserData.class);
        try {
            var result = handler.registerUser(newUser);
            return serializer.toJson(result);
        } catch (UserAlreadyTakenError e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            res.status(403);
            return serializer.toJson(errorMessage);
        }

    }

    private String login(Request req, Response res) {
        var loginUser = serializer.fromJson(req.body(), UserData.class);
        try {
            var result = handler.registerUser(loginUser);
            return serializer.toJson(result);
        } catch (UserAlreadyTakenError e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            res.status(403);
            return serializer.toJson(errorMessage);
        }
    }

    public String logout(Request req, Response res) {
        var logoutAuthToken = serializer.fromJson(req.body(), String.class);
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
