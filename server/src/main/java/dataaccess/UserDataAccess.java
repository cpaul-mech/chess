package dataaccess;

import model.UserData;

public interface UserDataAccess {
    public UserData getUser(String username);
}
