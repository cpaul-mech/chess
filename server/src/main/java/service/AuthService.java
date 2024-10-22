package service;

import dataaccess.AuthDataAccess;
import dataaccess.MemoryAuthDAO;

public class AuthService {
    private final AuthDataAccess _aDAO;

    public AuthService(AuthDataAccess aDAO) { //optional dataAccess constructor
        _aDAO = aDAO;
    }

    public AuthService() { //default constructor.
        _aDAO = new MemoryAuthDAO();
    }
}
