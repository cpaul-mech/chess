package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDataAccess {
    public int createGame(String gameName) throws DataAccessException;

    public GameData getGame(int gameID) throws DataAccessException;

    public Collection<GameData> listGames() throws DataAccessException;

    public void updateGame(int gameIDtoChange, GameData replacementGame);

    public void clearGameDB() throws DataAccessException;

    public int dbSize() throws DataAccessException;
}
