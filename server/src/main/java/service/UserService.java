package service;

import dataaccess.MemoryUserDAO;
import dataaccess.UserDataAccess;
import org.eclipse.jetty.server.Authentication;

public class UserService {
    private final UserDataAccess _uDAO;

    public UserService(UserDataAccess uDAO) {
        _uDAO = uDAO;
    }

    public UserService() {
        _uDAO = new MemoryUserDAO();
    }
}
