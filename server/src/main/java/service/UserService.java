package service;

import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import exceptions.BadServiceRequest;
import exceptions.UnauthorizedAccessError;
import exceptions.UserAlreadyTakenError;
import model.AuthData;
import model.UserData;

import java.util.Collection;
import java.util.Objects;

public class UserService {
    private final UserDataAccess userDataAccess;
    private final AuthService authService;

    public UserService(UserDataAccess uDAO, AuthDataAccess aDAO) {
        userDataAccess = uDAO;
        authService = new AuthService(aDAO);
    }

    public AuthData login(UserData userDataNullEmail) {
        var result = userDataAccess.getUser(userDataNullEmail.username());
        if (result != null && Objects.equals(result.username(), userDataNullEmail.username())
                && Objects.equals(result.password(), userDataNullEmail.password())) { //checking to make sure that
            //username and password both match the records.
            return authService.createAuthData(userDataNullEmail.username());
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
        var userResult = userDataAccess.getUser(userData.username()); //can be either null or not null.
        if (userResult == null) {
            //there was no user found in the database by that name!!
            userDataAccess.createUser(userData);
            return authService.createAuthData(userData.username());
        } else {
            throw new UserAlreadyTakenError("Error: already taken");
        }
    }

    public Collection<UserData> listUsers() {
        var allUsers = userDataAccess.listUsers();
        return allUsers;
    }

    public UserData getUser(String username) {
        return userDataAccess.getUser(username);
    }

    public void clearUserDB() {
        userDataAccess.clearUsers();
    }

    public int getUserDBsize() {
        return userDataAccess.dbSize();
    }

    public AuthService getAuthService() {
        return authService;
    }
}
