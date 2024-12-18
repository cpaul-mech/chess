package dataaccess;

import model.AuthData;

public interface AuthDataAccess {
    public int dbSize() throws DataAccessException;

    public void clearAuthDB() throws DataAccessException;

    public AuthData getAuthData(String authToken) throws DataAccessException;

    public void addAuthData(AuthData newAuthData) throws DataAccessException;

    public void deleteAuthData(AuthData authData) throws DataAccessException;
}
