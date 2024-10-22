package service;

import model.AuthData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthServiceTests {
    private final AuthService authService = new AuthService();

    @Test
    public void clear() {
        authService.clearAuthDB();
        assertEquals(authService.dbSize(), 0);
    }

}
