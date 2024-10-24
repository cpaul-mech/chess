package dataaccess;

import model.UserData;

import java.util.Collection;

public interface UserDataAccess {
    public void createUser(UserData userData);

    public UserData getUser(String username);

    public void clearUsers();

    public int dbSize();

    public Collection<UserData> listUsers();
}
