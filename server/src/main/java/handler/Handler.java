package handler;

import dataaccess.*;
import exceptions.UnauthorizedAccessError;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.mindrot.jbcrypt.BCrypt;
import service.*;

import java.sql.Struct;
import java.util.Collection;

public class Handler {//this class will be used to call all the various services
    private final GameDataAccess gameDataAccess = new MemoryGameDAO();
    private final UserDataAccess userDataAccess = new MemoryUserDAO();
    private final AuthDataAccess authDataAccess = new MemoryAuthDAO();
    private final GameService gameService = new GameService(gameDataAccess, authDataAccess);
    private final UserService userService = new UserService(userDataAccess, authDataAccess);
    private final AuthService authService = new AuthService(authDataAccess);

    public AuthData registerUser(UserData userData) {
        //I'm going to replace the password here with a hashed password string.
        return userService.registerUser(userData);
    }

    public LoginResponse login(UserData userDataNullEmail) {
        AuthData authData = userService.login(userDataNullEmail);
        return new LoginResponse(authData.username(), authData.authToken());
    }

    public void logout(String authToken) {
        authService.logout(authToken);
    }

    public Collection<GameData> listGames(String authToken) {
        if (authService.verifyAuthToken(authToken)) {
            return gameService.listGames();
        } else {
            throw new UnauthorizedAccessError("Error: unauthorized");
        }
    }

    public int createGame(String authToken, String gameName) {
        if (authService.verifyAuthToken(authToken)) {
            return gameService.createGame(gameName);
        } else {
            throw new UnauthorizedAccessError("Error: unauthorized");
        }
    }

    public void updateGamePlayer(String authToken, String color, int gameID) throws DataAccessException {
        if (authService.verifyAuthToken(authToken)) {
            String username = authService.getAuthData(authToken).username();
            gameService.updateGame(color, gameID, username);
        } else {
            throw new UnauthorizedAccessError("Error: unauthorized");
        }
    }

    public void clearAllDB() throws DataAccessException {
        userService.clearUserDB();
        gameService.clearGameDB();
        authService.clearAuthDB();
    }
}
