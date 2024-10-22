package service;

import dataaccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    private final UserService _user_service = new UserService();

    @Test
    public void clearUserDB() {
        _user_service.clearUserDB();
        assertEquals(_user_service.getUserDBsize(), 0);
    }

    @Test
    public void registerUser() {
        var newUser = new UserData("cpaul", "asdhfasdf", "sillybilly@gmail.com");
        var user = _user_service.registerUser(newUser);
        assertEquals(_user_service.getUserDBsize(), 1);
        assertEquals(_user_service.getUser(newUser.username()), user);
    }

    @Test
    public void duplicateUser() {
        var newUser = new UserData("cpaul", "asdhfasdf", "sillybilly@gmail.com");
        var user = _user_service.registerUser(newUser);
        var allUsers = _user_service.listUsers();
        assertThrows(UserAlreadyTakenError.class, () -> _user_service.registerUser(newUser));
    }
}
