package dataaccess;

import model.UserData;

import java.util.Collection;

public interface UserDataAccess {
    void createUser(UserData userData) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clearUsers() throws DataAccessException;

    int dbSize() throws DataAccessException;

    Collection<UserData> listUsers();
}
