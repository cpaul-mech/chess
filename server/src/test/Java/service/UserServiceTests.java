package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTests {
    private final UserService _user_service = new UserService();

    @Test
    public void clearUserDB() {
        _user_service.clearUserDB();
        assertEquals(_user_service.getUserDBsize(), 0);
    }
}
