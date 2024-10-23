package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthServiceTests {
    private final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    private final MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    private final UserService _user_service = new UserService(memoryUserDAO, memoryAuthDAO);
    private final AuthService authService = _user_service.getAuthService();


    @Test
    public void clear() {
        authService.clearAuthDB();
        assertEquals(authService.dbSize(), 0);
    }

    @Test
    public void logout(){
        var newUser = new UserData("crp","abcd","helpme@hotmail.com");
        var newUserAuthData = _user_service.registerUser(newUser);
        var result = authService.getAuthData(newUserAuthData.authToken());
        if(result == null){
            throw
        }
    }
}
