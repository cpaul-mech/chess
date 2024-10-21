package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDataAccess {
    final private Map<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        UserData newUser = new UserData(username, password, email);
        users.put(username, newUser);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username); //this may return null!!
    }

    @Override
    public void clearUsers() {
        //clear all the users in the memory
        users.clear(); //I hope that this will work.

    }
}
