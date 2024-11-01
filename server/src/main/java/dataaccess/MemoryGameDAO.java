package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDataAccess {
    private final Map<Integer, GameData> gameDB = new HashMap<>();
    public int gameID;

    public MemoryGameDAO() {
        gameID = 0;
    }

    @Override
    public int createGame(String gameName) {
        gameID += 1;
        ChessGame chessGame = new ChessGame();
        var newGame = new GameData(gameID, null, null, gameName, chessGame);
        gameDB.put(gameID, newGame);
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) {
        return gameDB.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        //need to lop off the games part of the GameData objects.
        ArrayList<GameData> gamesList = new ArrayList<>();
        var keySet = gameDB.keySet();
        for (Integer key : keySet) {
            var curGameData = gameDB.get(key);
            String whiteUsername = curGameData.whiteUsername();
            String blackUsername = curGameData.blackUsername();
            GameData truncGameData = new GameData(curGameData.gameID(), whiteUsername, blackUsername, curGameData.gameName(), null);
            gamesList.add(truncGameData);
        }
        return gamesList;
    }

    @Override
    public void updateGame(int gameIDtoChange, GameData replacementGame) {
        gameDB.replace(gameIDtoChange, replacementGame);
    }

    @Override
    public void clearGameDB() {
        gameDB.clear();
    }

    public int dbSize() {
        //returns the size of the database
        return gameDB.size();
    }
}
