package server;

import com.google.gson.Gson;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDataAccess;
import model.UserData;
import spark.*;

public class Server {
    private final UserDataAccess userDB = new MemoryUserDAO();
    private final Gson serializer = new Gson();

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post()

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private String createUser(Request req, Response res){
        var newUser = serializer.fromJson(req.body(), UserData.class);
        var result = service.registerUser(newUser;
        return serializer.toJson(result);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
