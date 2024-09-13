package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition _startPosition;
    private ChessPosition _endPosition;
    private final ChessPiece.PieceType _promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        _startPosition = startPosition;
        _endPosition = endPosition;
        this._promotionPiece = promotionPiece;
    }
    /* TODO: Add a toString method to improve how this functions. */
    @Override
    public String toString() {
        //This will print the start position
        var startPos = _startPosition.toString();
        var endPos = _endPosition.toString();
        var promPiece = _promotionPiece.toString();
        return "move: "+startPos+" "+endPos+" "+promPiece;
    }
    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return _startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return _endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        throw new RuntimeException("Not implemented");
    }
}
