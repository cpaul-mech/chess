package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDataAccess {
    final private Map<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData userData) {
        users.put(userData.username(), userData);
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

    public int dbSize() {
        return users.size();
    }
}
