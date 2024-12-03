package commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private ChessMove moveMake;

    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        moveMake = move;
    }
}
