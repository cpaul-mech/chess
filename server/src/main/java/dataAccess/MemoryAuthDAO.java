package dataAccess;

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

    public AuthData addAuthData(AuthData authData) {
        authDB.put(authData.authToken(), authData);
        return authData;
    }

    @Override
    public void deleteAuthData(AuthData authData) {
        authDB.remove(authData.authToken());
    }

    public AuthData getAuthData(String authToken) {
        return authDB.get(authToken);
    }

}
