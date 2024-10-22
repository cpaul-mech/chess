package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthServiceTests {
    private final AuthService authService = new AuthService();

    @Test
    public void clear() {
        authService.clearAuthDAO();
        assertEquals(authService.dbSize(), 0);
    }
}
