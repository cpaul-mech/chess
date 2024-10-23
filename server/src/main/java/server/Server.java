package server;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import handler.Handler;
import service.AuthService;
import service.GameService;
import service.UserService;
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
        Spark.delete("/db", this::clearAllDB);


        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private String clearAllDB(Request req, Response res) {
        handler.clearallDB();
        res.status(200);
        return "";
    }


//    private String createUser(Request req, Response res){
//        var newUser = serializer.fromJson(req.body(), UserData.class);
//        var result = UserService.registerUser(newUser);
//        return serializer.toJson(result);
//    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
