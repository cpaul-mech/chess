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
    @Override
    public String toString() {
        //This will print the start position
        var startPos = _startPosition.toString();
        var endPos = _endPosition.toString();
        if(_promotionPiece != null){
            var promPiece = _promotionPiece.toString();
        }

        return "move: "+startPos+" "+endPos;
    }
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true; //checks for if the memory addresses are the same.
        if(obj == null || getClass() != obj.getClass()) return false; //checks for if the object's class is equal to the current class
        ChessMove move = (ChessMove) obj; //sets a new variable move equal to the object and casts object to a ChessMove Object.
        return (_startPosition.equals(move._startPosition) && _endPosition.equals(move._endPosition) &&
                _promotionPiece == move._promotionPiece);
    }
    @Override
    public int hashCode() {
        int promotionCode;
        if (_promotionPiece == null) promotionCode = 9;
        else promotionCode = _promotionPiece.ordinal();
        return (71*_startPosition.hashCode()) + _endPosition.hashCode() + promotionCode;
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
        return _promotionPiece;
    }
}
