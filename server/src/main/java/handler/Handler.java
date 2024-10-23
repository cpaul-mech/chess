package handler;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.UnauthorizedAccessError;
import service.UserService;

import java.util.Objects;

public class Handler {//this class will be used to call all the various services
    private GameDataAccess gameDataAccess = new MemoryGameDAO();
    private UserDataAccess userDataAccess = new MemoryUserDAO();
    private AuthDataAccess authDataAccess = new MemoryAuthDAO();
    private final GameService gameService = new GameService(gameDataAccess);
    private final UserService userService = new UserService(userDataAccess, authDataAccess);
    private final AuthService authService = new AuthService(authDataAccess);

    public void registerUser(UserData userData) {
        userService.registerUser(userData);
    }

    public AuthData login(UserData userDataNullEmail) {
        var authData = userService.login(userDataNullEmail);
        return authData;
    }

    public void clearallDB() {
        userService.clearUserDB();
        gameService.clearGameDB();
        authService.clearAuthDB();
    }
}
