package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AuthServiceTests {
    private final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    private final MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    private final UserService userService = new UserService(memoryUserDAO, memoryAuthDAO);
    private final AuthService authService = userService.getAuthService();


    @BeforeEach
    public void clear() throws DataAccessException {
        authService.clearAuthDB();
        assertEquals(authService.dbSize(), 0);
    }

    @Test
    public void addAuthToken() {
        var newUser = new UserData("crp", "abcd", "helpme@hotmail.com");
        var newUserAuthData = userService.registerUser(newUser);
        var result = authService.getAuthData(newUserAuthData.authToken());
        assertEquals(result.getClass(), AuthData.class);
    }

    @Test
    public void logout() {
        var newUser = new UserData("crp", "abcd", "helpme@hotmail.com");
        var newUserAuthData = userService.registerUser(newUser);
        var result = authService.getAuthData(newUserAuthData.authToken());
        //now both newUserAuthData and result should be the same
        boolean successful = authService.logout(result.authToken());
        assertNull(authService.getAuthData(newUserAuthData.authToken()));
    }
}
