package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.MemoryGameDAO;
import exceptions.BadServiceRequest;
import exceptions.UserAlreadyTakenError;
import model.GameData;

import java.util.Collection;

public class GameService {
    private final GameDataAccess gameDataAccess;

    public GameService(GameDataAccess gDAO) {
        gameDataAccess = gDAO;
    }

    public GameService() {
        gameDataAccess = new MemoryGameDAO();
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return gameDataAccess.getGame(gameID);
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return gameDataAccess.listGames(); //list games will only be called if the user is authorized.
    }

    public int createGame(String gameName) throws DataAccessException {
        return gameDataAccess.createGame(gameName);
    }

    public void clearGameDB() throws DataAccessException {
        gameDataAccess.clearGameDB();
    }

    public int sizeof() throws DataAccessException {
        return gameDataAccess.dbSize();
    }

    public void updateGame(String updateColor, int gameID, String newUsername) throws DataAccessException {
        //what happens if the gameID is invalid? we return null... but that doesn't make this stuff true
        ChessGame.TeamColor colorToChange;
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
                    gameDataAccess.updateGame(gameID, newGame);
                } else {
                    //username already taken!!
                    throw new UserAlreadyTakenError("Error: already taken"); //error code USERNAME ALREADY TAKEN.
                }
            } else {
                if (game.whiteUsername() == null) {
                    var newGame = new GameData(gameID, newUsername, game.blackUsername(), game.gameName(), game.game());
                    gameDataAccess.updateGame(gameID, newGame);
                } else {
                    throw new UserAlreadyTakenError("Error: already taken"); //error code USERNAME ALREADY TAKEN.
                }
            }

        }
    }
}
