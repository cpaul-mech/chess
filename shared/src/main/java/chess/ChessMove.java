package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    @Override
    public String toString() {
        //This will print the start position
        var startPos = startPosition.toString();
        var endPos = endPosition.toString();
        if (promotionPiece != null) {
            var promPiece = promotionPiece.toString();
        }

        return "move: " + startPos + " " + endPos;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; //checks for if the memory addresses are the same.
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; //checks for if the object's class is equal to the current class
        }
        ChessMove move = (ChessMove) obj; //sets a new variable move equal to the object and casts object to a ChessMove Object.
        return (startPosition.equals(move.startPosition) && endPosition.equals(move.endPosition) &&
                promotionPiece == move.promotionPiece);
    }

    @Override
    public int hashCode() {
        int promotionCode;
        if (promotionPiece == null) {
            promotionCode = 9;
        } else {
            promotionCode = promotionPiece.ordinal();
        }
        return (71 * startPosition.hashCode()) + endPosition.hashCode() + promotionCode;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }
}
