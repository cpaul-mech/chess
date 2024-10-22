package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDataAccess {
    private final Map<Integer, GameData> gameDB = new HashMap<>();

    public MemoryGameDAO() {
    }

    @Override
    public void createGame() {

    }

    @Override
    public GameData getGame(int gameID) {
        return gameDB.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return gameDB.values();
    }

    @Override
    public GameData updateGame(int gameIDtoChange, GameData replacementGame) {
        return null;
    }

    @Override
    public void clearDB() {
        gameDB.clear();
    }
}
