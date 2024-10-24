package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDataAccess {
    public int createGame(String gameName);

    public GameData getGame(int gameID);

    public Collection<GameData> listGames();

    public void updateGame(int gameIDtoChange, GameData replacementGame);

    public void clearDB();

    public int dbSize();
}
