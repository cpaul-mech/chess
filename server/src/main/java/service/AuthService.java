package service;

import dataaccess.AuthDataAccess;
import dataaccess.MemoryAuthDAO;
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

    public void clearAuthDB() {
        authDataAccess.clearAuthDB();
    }

    public AuthData createAuthData(String username) {//im going to add smart functionality to this, there should only ever be one authtoken per user
        var newAuthData = new AuthData(generateToken(), username);
        authDataAccess.addAuthData(newAuthData);
        return newAuthData;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public int dbSize() {
        return authDataAccess.dbSize();
    }

    public AuthData getAuthData(String authToken) {
        return authDataAccess.getAuthData(authToken);
    }

    public boolean verifyAuthToken(String authToken) {
        var authData = getAuthData(authToken);
        if (authData == null) {
            return false;
        } else {
            return authData.username() != null && authData.authToken() != null;
        }
    }

    public boolean logout(String authToken) {
        if (verifyAuthToken(authToken)) {
            deleteAuthData(getAuthData(authToken));
            return true;
        } else {
            throw new UnauthorizedAccessError("Error: unauthorized");
        }
    }

    public void deleteAuthData(AuthData authData) {
        authDataAccess.deleteAuthData(authData);
    }
}
