package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import exceptions.BadServiceRequest;
import exceptions.UnauthorizedAccessError;
import exceptions.UserAlreadyTakenError;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collection;
import java.util.Objects;

public class UserService {
    private final UserDataAccess userDataAccess;
    private final AuthService authService;

    public UserService(UserDataAccess uDAO, AuthDataAccess aDAO) {
        userDataAccess = uDAO;
        authService = new AuthService(aDAO);
    }

    public AuthData login(UserData userDataNullEmail) throws DataAccessException {
        var result = userDataAccess.getUser(userDataNullEmail.username());
        //here in the service level is where we're comparing the passwords. so I will be using BCrypt.checkpw
        if (result != null && Objects.equals(result.username(), userDataNullEmail.username())
                && BCrypt.checkpw(userDataNullEmail.password(), result.password())) { //checking to make sure that
            //username and password both match the records.
            return authService.createAuthData(userDataNullEmail.username());
        } else {
            //this user isn't registered, or their username, password, and email do not match.
            throw new UnauthorizedAccessError("Error: unauthorized");
        }
    }

    public AuthData registerUser(UserData userData) throws UserAlreadyTakenError, DataAccessException {
        if (userData.username() == null || userData.password() == null || userData.email() == null) {
            //this is a bad request!! cannot store!!
            throw new BadServiceRequest("Error: bad request");
        }
        var userResult = userDataAccess.getUser(userData.username()); //can be either null or not null.
        if (userResult == null) {
            //there was no user found in the database by that name!!
            String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
            UserData userDataHashedPassword = new UserData(userData.username(), hashedPassword, userData.email());
            userDataAccess.createUser(userDataHashedPassword);
            return authService.createAuthData(userDataHashedPassword.username());
        } else {
            throw new UserAlreadyTakenError("Error: already taken");
        }
    }

    public Collection<UserData> listUsers() {
        var allUsers = userDataAccess.listUsers();
        return allUsers;
    }

    public UserData getUser(String username) throws DataAccessException {
        return userDataAccess.getUser(username);
    }

    public void clearUserDB() throws DataAccessException {
        userDataAccess.clearUsers();
    }

    public int getUserDBsize() throws DataAccessException {
        return userDataAccess.dbSize();
    }

    public AuthService getAuthService() {
        return authService;
    }
}
