package handler;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.*;

import java.util.ArrayList;
import java.util.Collection;

public class Handler {//this class will be used to call all the various services
    private GameDataAccess gameDataAccess = new MemoryGameDAO();
    private UserDataAccess userDataAccess = new MemoryUserDAO();
    private AuthDataAccess authDataAccess = new MemoryAuthDAO();
    private final GameService gameService = new GameService(gameDataAccess, authDataAccess);
    private final UserService userService = new UserService(userDataAccess, authDataAccess);
    private final AuthService authService = new AuthService(authDataAccess);

    public AuthData registerUser(UserData userData) {
        return userService.registerUser(userData);
    }

    public LoginResponse login(UserData userDataNullEmail) {
        var authData = userService.login(userDataNullEmail);
        LoginResponse loginResponse = new LoginResponse(authData.username(), authData.authToken());
        return loginResponse;
    }

    public void logout(String authToken) {
        var success = authService.logout(authToken);
    }

    public Collection<GameData> listGames(String authToken) {
        if (authService.verifyAuthToken(authToken)) {
            var gamesList = gameService.listGames();
            if (gamesList == null || gamesList.isEmpty()) {
                return gamesList;
            } else {
                return gamesList;
            }
        } else {
            throw new UnauthorizedAccessError("Error: unauthorized");
        }
    }

    public Collection<GameData> processGameDataForReturn(ArrayList<GameData> games) {
        //this function will take every game in gamesList and set the "game" field to null
        for (int i = 0; i < games.size(); i++) {
            var gameData = games.get(i);
            var truncGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), null);
            games.remove(gameData);
            games.add(truncGameData);
        }
        return games;
    }

    public int createGame(String authToken, String gameName) {
        if (authService.verifyAuthToken(authToken)) {
            return gameService.createGame(gameName);
        } else {
            //return bad things, unauthorized user!!!
            throw new UnauthorizedAccessError("Error: unauthorized");
        }
    }

    public void updateGamePlayer(String authToken, String color, int gameID) throws DataAccessException {
        if (authService.verifyAuthToken(authToken)) {
            String username = authService.getAuthData(authToken).username();
            gameService.updateGame(color, gameID, username);
            //return bad juju, UNAUTHORIZED USER!!!
        } else {
            throw new UnauthorizedAccessError("Error: unauthorized");
        }
    }

    public void clearAllDB() {
        userService.clearUserDB();
        gameService.clearGameDB();
        authService.clearAuthDB();
    }
}
