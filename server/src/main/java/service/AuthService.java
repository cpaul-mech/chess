package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import exceptions.UnauthorizedAccessError;
import model.AuthData;

import java.util.UUID;

public class AuthService {
    private final AuthDataAccess authDataAccess;

    public AuthService(AuthDataAccess aDAO) { //optional dataAccess constructor
        authDataAccess = aDAO;
    }

    public AuthService() { //default constructor.
        authDataAccess = new MemoryAuthDAO();
    }

    public void clearAuthDB() throws DataAccessException {
        authDataAccess.clearAuthDB();
    }

    public AuthData createAuthData(String username) throws DataAccessException {//im going to add smart functionality
        // to this, there should only ever be one authtoken per user
        var newAuthData = new AuthData(generateToken(), username);
        authDataAccess.addAuthData(newAuthData);
        return newAuthData;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public int dbSize() throws DataAccessException {
        return authDataAccess.dbSize();
    }

    public AuthData getAuthData(String authToken) throws DataAccessException {
        return authDataAccess.getAuthData(authToken);
    }

    public boolean verifyAuthToken(String authToken) throws DataAccessException {
        var authData = getAuthData(authToken);
        if (authData == null) {
            return false;
        } else {
            return authData.username() != null && authData.authToken() != null;
        }
    }

    public boolean logout(String authToken) throws DataAccessException {
        if (verifyAuthToken(authToken)) {
            deleteAuthData(getAuthData(authToken));
            return true;
        } else {
            throw new UnauthorizedAccessError("Error: unauthorized");
        }
    }

    public void deleteAuthData(AuthData authData) throws DataAccessException {
        authDataAccess.deleteAuthData(authData);
    }
}
