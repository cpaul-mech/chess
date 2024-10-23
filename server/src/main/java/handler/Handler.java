package handler;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.UnauthorizedAccessError;
import service.UserService;

import java.util.Collection;
import java.util.Objects;

public class Handler {//this class will be used to call all the various services
    private GameDataAccess gameDataAccess = new MemoryGameDAO();
    private UserDataAccess userDataAccess = new MemoryUserDAO();
    private AuthDataAccess authDataAccess = new MemoryAuthDAO();
    private final GameService gameService = new GameService(gameDataAccess, authDataAccess);
    private final UserService userService = new UserService(userDataAccess, authDataAccess);
    private final AuthService authService = new AuthService(authDataAccess);

    public void registerUser(UserData userData) {
        userService.registerUser(userData);
    }

    public AuthData login(UserData userDataNullEmail) {
        var authData = userService.login(userDataNullEmail);
        return authData;
    }

    public void logout(String authToken) {
        authService.logout(authToken);
    }

    public Collection<GameData> listGames(String authToken) {
        if (authService.verifyAuthToken(authToken)) {
            var gamesList = gameService.listGames();
            if (gamesList == null || gamesList.isEmpty()) {
                return null;
            } else {
                return gamesList;
            }
        } else {
            //return the jsonthingy //UNAUTHORIZED USER!!
            return null;
        }
    }

    public int createGame(String authToken, String gameName) {
        if (authService.verifyAuthToken(authToken)) {
            return gameService.createGame(gameName);
        } else {
            //return bad things, unauthorized user!!!
            return 0; //UNAUTHORIZED USER!!
        }
    }

    public void clearallDB() {
        userService.clearUserDB();
        gameService.clearGameDB();
        authService.clearAuthDB();
    }
}
