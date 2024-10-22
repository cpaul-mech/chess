package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.Map;

public interface UserDataAccess {
    //TODO: What kinds of functions need to go here again?
    public void createUser(UserData userData);

    public UserData getUser(String username);

    public void clearUsers();

    public int dbSize();

    public Collection<UserData> listUsers();
}
