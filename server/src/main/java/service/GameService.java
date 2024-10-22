package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.MemoryGameDAO;
import dataaccess.UserDataAccess;
import model.UserData;

public class GameService {
    private final GameDataAccess _gDAO;

    public GameService(GameDataAccess gDAO) {
        _gDAO = gDAO;
    }

    public GameService() {
        _gDAO = new MemoryGameDAO();
    }


    public void clearGameDB() {
        _gDAO.clearDB();
    }

    public int sizeof() {
        return _gDAO.dbSize();
    }
}
