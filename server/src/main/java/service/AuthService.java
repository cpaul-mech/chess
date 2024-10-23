package service;

import dataaccess.AuthDataAccess;
import dataaccess.MemoryAuthDAO;
import model.AuthData;

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

    public AuthData createAuthData(String username) {
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
}
