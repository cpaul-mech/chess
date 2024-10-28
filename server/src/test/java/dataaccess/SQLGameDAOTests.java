package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAOTests implements GameDataAccess {
    //TODO: IMPLEMENT THESE METHODS!!
    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(int gameIDtoChange, GameData replacementGame) {

    }

    @Override
    public void clearDB() {

    }

    @Override
    public int dbSize() {
        return 0;
    }
}
