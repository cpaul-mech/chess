package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDataAccess {
    private final Map<Integer, GameData> gameDB = new HashMap<>();
    public int gameID;

    public MemoryGameDAO() {
        gameID = 0; //starting value
    }

    @Override
    public int createGame(String gameName) {
        gameID += 1;
        ChessGame chessGame = new ChessGame();
        var newGame = new GameData(gameID, null, null, gameName, chessGame);
        return gameID;
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

    public int dbSize() {
        //returns the size of the database
        return gameDB.size();
    }
}
