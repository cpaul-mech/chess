package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.UserData;

public class Service {
    private final GameDataAccess _gDAO;
    private final UserDataAccess _uDAO;
    private final AuthDataAccess _aDAO;

    public Service(GameDataAccess gDAO, UserDataAccess uDAO, AuthDataAccess aDAO) {
        _gDAO = gDAO;
        _uDAO = uDAO;
        _aDAO = aDAO;
    }

    public UserData registerUser(UserData newUser) {
        return null;
    }

    public void clearGameDB() {
        _gDAO.clearDB();
    }
}
