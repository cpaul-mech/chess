package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDataAccess;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class UserService {
    private final UserDataAccess _uDAO;
    private final AuthService _authService;

    public UserService(UserDataAccess uDAO) {
        _uDAO = uDAO;
        if (uDAO.getClass() == MemoryUserDAO.class) {
            _authService = new AuthService(); //there might be a bug here because I might need to initialize with
        } else {
            //need to place the other class here later, but for now, leave them both as default
            _authService = new AuthService();
        }
    }

    public UserService() {
        _uDAO = new MemoryUserDAO();
        _authService = new AuthService();
    }

    public UserData registerUser(UserData userData) throws UserAlreadyTakenError {
        var userResult = _uDAO.getUser(userData.username()); //can be either null or not null.
        if (userResult == null) {
            //there was no user found in the database by that name!!
            _uDAO.createUser(userData);
            _authService.createAuthData(userData.username());
            return userData;
        } else {
            throw new UserAlreadyTakenError("User was already taken");
        }
    }

    public Collection<UserData> listUsers() {
        var allUsers = _uDAO.listUsers();
        return allUsers;
    }

    public UserData getUser(String username) {
        return _uDAO.getUser(username);
    }

    public void clearUserDB() {
        _uDAO.clearUsers();
    }

    public int getUserDBsize() {
        return _uDAO.dbSize();
    }
}
