package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDataAccess {
    //the key, value pair is <AuthToken, AuthData object>
    private final Map<String, AuthData> authDB = new HashMap<>();

    public int dbSize() {
        return authDB.size();
    }

    public void clearAuthDB() {
        authDB.clear();
    }

    public void addAuthData(AuthData authData) {
        authDB.put(authData.authToken(), authData);
    }

    @Override
    public void deleteAuthData(AuthData authData) {
        authDB.remove(authData.authToken());
    }

    public AuthData getAuthData(String authToken) {
        return authDB.get(authToken);
    }

}
