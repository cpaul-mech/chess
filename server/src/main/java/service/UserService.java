package service;

import dataaccess.AuthDataAccess;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;

import java.util.Collection;
import java.util.Objects;

public class UserService {
    private final UserDataAccess _uDAO;
    private final AuthService _authService;

    public UserService(UserDataAccess uDAO, AuthDataAccess aDAO) {
        _uDAO = uDAO;
        _authService = new AuthService(aDAO);
    }

    public UserService() {
        _uDAO = new MemoryUserDAO();
        _authService = new AuthService();
    }

    public AuthData login(UserData userDataNullEmail) {
        var result = _uDAO.getUser(userDataNullEmail.username());
        if (result != null && Objects.equals(result.username(), userDataNullEmail.username())
                && Objects.equals(result.password(), userDataNullEmail.password())) { //checking to make sure that
            //username and password both match the records.
            return _authService.createAuthData(userDataNullEmail.username());
        } else {
            //this user isn't registered, or their username, password, and email do not match.
            throw new UnauthorizedAccessError("Error: unauthorized");
        }
    }

    public AuthData registerUser(UserData userData) throws UserAlreadyTakenError {
        if (userData.username() == null || userData.password() == null || userData.email() == null) {
            //this is a bad request!! cannot store!!
            throw new BadServiceRequest("Error: bad request");
        }
        //TODO: IMPLEMENT BAD REQUEST AND WEIRD REQUEST FUNCTIONALITY.
        var userResult = _uDAO.getUser(userData.username()); //can be either null or not null.
        if (userResult == null) {
            //there was no user found in the database by that name!!
            _uDAO.createUser(userData);
            return _authService.createAuthData(userData.username());
        } else {
            throw new UserAlreadyTakenError("Error: already taken");
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

    public AuthService getAuthService() {
        return _authService;
    }
}
