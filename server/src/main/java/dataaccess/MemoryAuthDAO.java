package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDataAccess {
    //the key, value pair is <AuthToken, AuthData object>
    private Map<String, AuthData> authDB = new HashMap<>();

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

}
