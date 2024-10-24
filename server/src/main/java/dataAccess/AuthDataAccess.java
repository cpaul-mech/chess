package dataAccess;

import model.AuthData;

public interface AuthDataAccess {
    public int dbSize();

    public void clearAuthDB();

    public AuthData getAuthData(String authToken);

    public AuthData addAuthData(AuthData newAuthData);

    public void deleteAuthData(AuthData authData);
}