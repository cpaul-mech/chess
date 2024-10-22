package service;

import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.UserData;

public class GameService {
    private final GameDataAccess gmDataAccess;

    public GameService(GameDataAccess gDAO) {
        gmDataAccess = gDAO;
    }

    public UserData registerUser(UserData newUser) {

    }

    public void clearGameDB() {
        gmDataAccess.clearDB();
    }
}
