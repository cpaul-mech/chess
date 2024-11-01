package dataaccess;

import model.UserData;

import java.util.Collection;

public interface UserDataAccess {
    void createUser(UserData userData) throws DataAccessException;

    UserData getUser(String username);

    void clearUsers() throws DataAccessException;

    int dbSize();

    Collection<UserData> listUsers();
}
