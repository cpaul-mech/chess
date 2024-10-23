package service;

import dataaccess.AuthDataAccess;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class AuthService {
    private final AuthDataAccess _aDAO;

    public AuthService(AuthDataAccess aDAO) { //optional dataAccess constructor
        _aDAO = aDAO;
    }

    public AuthService() { //default constructor.
        _aDAO = new MemoryAuthDAO();
    }

    public void clearAuthDB() {
        _aDAO.clearAuthDB();
    }

    public AuthData createAuthData(String username) {//im going to add smart functionality to this, there should only ever be one authtoken per user
        var newAuthData = new AuthData(generateToken(), username);
        _aDAO.addAuthData(newAuthData);
        return newAuthData;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public int dbSize() {
        return _aDAO.dbSize();
    }

    public AuthData getAuthData(String authToken) {
        return _aDAO.getAuthData(authToken);
    }

    public boolean logout(String AuthToken) {
        var authData = getAuthData(AuthToken);
        if (authData == null) {
            throw new UnauthorizedAccessError("Error: Unauthorized User");
        } else {
            deleteAuthData(authData);
            return true;
        }
    }

    public void deleteAuthData(AuthData authData) {
        _aDAO.deleteAuthData(authData);
    }
}
