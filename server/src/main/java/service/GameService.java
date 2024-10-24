package service;

import chess.ChessGame;
import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import dataAccess.MemoryGameDAO;
import model.GameData;

import java.util.Collection;

public class GameService {
    private final GameDataAccess _gDAO;
    private final AuthService _authService;

    public GameService(GameDataAccess gDAO, AuthDataAccess aDAO) {
        _gDAO = gDAO;
        _authService = new AuthService(aDAO);
    }

    public GameService() {
        _gDAO = new MemoryGameDAO();
        _authService = new AuthService();
    }

    public GameData getGame(int gameID) {
        return _gDAO.getGame(gameID);
    }

    public Collection<GameData> listGames() {
        return _gDAO.listGames(); //list games will only be called if the user is authorized.
    }

    public int createGame(String gameName) {
        int newGame = _gDAO.createGame(gameName);
        return newGame;
    }

    public void clearGameDB() {
        _gDAO.clearDB();
    }

    public int sizeof() {
        return _gDAO.dbSize();
    }

    public void updateGame(String updateColor, int gameID, String newUsername) throws DataAccessException {
        //what happens if the gameID is invalid? we return null... but that doesn't make this stuff true
        ChessGame.TeamColor colorToChange = null;
        if (updateColor == null) {
            throw new BadServiceRequest("Error: bad request");
        } else if (updateColor.equalsIgnoreCase("WHITE")) {
            colorToChange = ChessGame.TeamColor.WHITE;
        } else if (updateColor.equalsIgnoreCase("BLACK")) {
            colorToChange = ChessGame.TeamColor.BLACK;
        } else {
            //return BAD REQUEST CODE!!!
            throw new BadServiceRequest("Error: bad request");
        }
        var game = getGame(gameID);
        if (game == null) {
            throw new BadServiceRequest("Error: bad request"); //represents an error code.
        } else {
            if (colorToChange == ChessGame.TeamColor.BLACK) {
                //attempt to replace black team username
                if (game.blackUsername() == null) {
                    var newGame = new GameData(gameID, game.whiteUsername(), newUsername, game.gameName(), game.game());
                    _gDAO.updateGame(gameID, newGame);
                } else {
                    //username already taken!!
                    throw new UserAlreadyTakenError("Error: already taken"); //error code USERNAME ALREADY TAKEN.
                }
            } else {
                if (game.whiteUsername() == null) {
                    var newGame = new GameData(gameID, newUsername, game.blackUsername(), game.gameName(), game.game());
                    _gDAO.updateGame(gameID, newGame);
                } else {
                    throw new UserAlreadyTakenError("Error: already taken"); //error code USERNAME ALREADY TAKEN.
                }
            }

        }
    }
}