package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    private final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    private final MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    private final UserService _user_service = new UserService(memoryUserDAO, memoryAuthDAO);

    @Test
    public void clearUserDB() {
        _user_service.clearUserDB();
        assertEquals(_user_service.getUserDBsize(), 0);
    }

    @Test
    public void registerUser() {
        UserData newUser = new UserData("cpaul", "asdhfasdf", "sillybilly@gmail.com");
        AuthData user = _user_service.registerUser(newUser);
        assertEquals(_user_service.getUserDBsize(), 1);
        assertEquals(_user_service.getUser(newUser.username()), newUser);
    }

    @Test
    public void duplicateUser() {
        var newUser = new UserData("cpaul", "coolDude", "sillybilly@gmail.com");
        var user = _user_service.registerUser(newUser);
        var allUsers = _user_service.listUsers();
        assertThrows(UserAlreadyTakenError.class, () -> _user_service.registerUser(newUser));
    }

    @Test
    public void login() {
        var loginUser = new UserData("cpaul3", "coolDude", null);
        //make sure that you cannot log in if your username is not in the DB.
        assertThrows(UnauthorizedAccessError.class, () -> _user_service.login(loginUser));
        var newUser = new UserData("cpaul3", "coolDude", "sillybilly@gmail.com");
        AuthData newUserAuthData = _user_service.registerUser(newUser);
        assertNotEquals(newUserAuthData, _user_service.login(newUser));
    }
}
