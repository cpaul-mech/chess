package handler;

import dataaccess.*;
import service.AuthService;
import service.GameService;
import service.UserService;

public class Handler {//this class will be used to call all the various services
    private GameDataAccess gameDataAccess = new MemoryGameDAO();
    private UserDataAccess userDataAccess = new MemoryUserDAO();
    private AuthDataAccess authDataAccess = new MemoryAuthDAO();
    private GameService gameService = new GameService(gameDataAccess);
    private UserService userService;
    private AuthService authService;
}
