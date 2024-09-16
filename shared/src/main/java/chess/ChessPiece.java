package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor _color;
    private final PieceType _type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this._color = pieceColor; //possible error, I don't fully understand why the "this" command is used, or if it
        //should be used here.
        this._type = type;
    }
    // TODO: add a toString Method that prints out the teamColor and the PieceType.
    @Override
    public String toString() {
        String color = this._color.toString();
        String type = this._type.toString();
        return color + ", " + type;
    }
    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this._color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this._type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //I can see whether there are pieces on the board at a location by calling board.getPiece();
        //I would think that the board will return null object if it doesn't find anything? Or will it return 0?
        Collection<ChessMove> moves = new ArrayList<ChessMove>(); //a collection can be an ArrayList.
        return moves;
        /*
        This method is similar to ChessGame.validMoves, except it does not honor whose turn it is or check
        if the king is being attacked. This method does account for enemy and friendly pieces blocking movement paths.
        The pieceMoves method will need to take into account the type of piece, and the location of other pieces on the board.
         */
    }
    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        //TODO: use recursive helper functions to figure out what moves a bishop can take.
        return null;
    }
}
