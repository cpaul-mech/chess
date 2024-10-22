package dataaccess;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDataAccess {
    private Map<String, String> authDB = new HashMap<>();

    public int dbSize() {
        return authDB.size();
    }

    public void clearAuthDB() {
        authDB.clear();
    }
}
