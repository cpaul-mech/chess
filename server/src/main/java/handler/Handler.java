package handler;

import dataaccess.*;
import exceptions.UnauthorizedAccessError;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.*;

import java.util.Collection;

public class Handler {//this class will be used to call all the various services
    private final AuthDataAccess authDataAccess;

    private final GameService gameService;
    private final UserService userService;
    private final AuthService authService;

    public Handler() {
        try {
            GameDataAccess gameDataAccess = new SQLGameDAO();
            UserDataAccess userDataAccess = new SQLUserDAO();
            authDataAccess = new SQLAuthDAO();
            gameService = new GameService(gameDataAccess);
            userService = new UserService(userDataAccess, authDataAccess);
            authService = new AuthService(authDataAccess);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthService getAuthService() {
        return authService;
    }


    public AuthData registerUser(UserData userData) throws DataAccessException {
        //I'm going to replace the password here with a hashed password string.
        return userService.registerUser(userData);
    }

    public LoginResponse login(UserData userDataNullEmail) throws DataAccessException {
        AuthData authData = userService.login(userDataNullEmail);
        return new LoginResponse(authData.username(), authData.authToken());
    }

    public void logout(String authToken) throws DataAccessException {
        authService.logout(authToken);
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        if (authService.verifyAuthToken(authToken)) {
            return gameService.listGames();
        } else {
            throw new UnauthorizedAccessError("Error: unauthorized");
        }
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
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
