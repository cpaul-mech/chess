package dataaccess;

import model.GameData;

import java.util.ArrayDeque;
import java.util.ArrayList;

public interface GameDataAccess {
    public void createGame();

    public GameData getGame(int gameID);

    public ArrayList<GameData> listGames();

    public GameData updateGame(int gameIDtoChange, GameData replacementGame);

}
