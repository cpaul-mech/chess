package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exceptions.UnauthorizedAccessError;
import exceptions.UserAlreadyTakenError;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    private final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    private final MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    private final UserService userService = new UserService(memoryUserDAO, memoryAuthDAO);

    @Test
    public void clearUserDB() {
        userService.clearUserDB();
        assertEquals(userService.getUserDBsize(), 0);
    }

    @Test
    public void registerUser() {
        UserData newUser = new UserData("cpaul", "asdhfasdf", "sillybilly@gmail.com");
        AuthData user = userService.registerUser(newUser);
        assertEquals(userService.getUserDBsize(), 1);
        assertEquals(userService.getUser(newUser.username()), newUser);
    }

    @Test
    public void duplicateUser() {
        var newUser = new UserData("cpaul", "coolDude", "sillybilly@gmail.com");
        var user = userService.registerUser(newUser);
        var allUsers = userService.listUsers();
        assertThrows(UserAlreadyTakenError.class, () -> userService.registerUser(newUser));
    }

    @Test
    public void login() {
        var loginUser = new UserData("cpaul3", "coolDude", null);
        //make sure that you cannot log in if your username is not in the DB.
        assertThrows(UnauthorizedAccessError.class, () -> userService.login(loginUser));
        var newUser = new UserData("cpaul3", "coolDude", "sillybilly@gmail.com");
        AuthData newUserAuthData = userService.registerUser(newUser);
        assertNotEquals(newUserAuthData, userService.login(newUser));
    }
}
