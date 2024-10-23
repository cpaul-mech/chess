package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.MemoryGameDAO;
import model.GameData;

import java.util.Collection;

public class GameService {
    private final GameDataAccess _gDAO;
    private final AuthService _authService;

    public GameService(GameDataAccess gDAO, AuthDataAccess aDAO) {
        _gDAO = gDAO;
        _authService = new AuthService(aDAO);
    }

    public GameService() {
        _gDAO = new MemoryGameDAO();
        _authService = new AuthService();
    }

    public GameData getGame(int gameID) {
        return _gDAO.getGame(gameID);
    }

    public Collection<GameData> listGames() {
        return _gDAO.listGames(); //list games will only be called if the user is authorized.
    }

    public int createGame(String gameName) {
        int newGame = _gDAO.createGame(gameName);
        return newGame;
    }

    public void clearGameDB() {
        _gDAO.clearDB();
    }

    public int sizeof() {
        return _gDAO.dbSize();
    }
}
