package dataaccess;

import model.GameData;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

public interface GameDataAccess {
    public void createGame();

    public GameData getGame(int gameID);

    public Collection<GameData> listGames();

    public GameData updateGame(int gameIDtoChange, GameData replacementGame);

    public void clearDB();

    public int dbSize();
}
