package dataaccess;

import model.UserData;

public interface UserDataAccess {
    //TODO: What kinds of functions need to go here again?
    public void createUser(String username, String password, String email) throws DataAccessException;

    public UserData getUser(String username);

    public void clearUsers();

}
