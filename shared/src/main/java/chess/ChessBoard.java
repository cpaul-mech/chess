package chess;

import java.util.ArrayList;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] _board = new ChessPiece[10][10]; //an array of ChessPiece Objects!
    public ChessBoard() {
        
    }
    @Override
    public boolean equals(Object obj){
        if(this == obj) return true; //checks for if the memory addresses are the same.
        if(obj == null || getClass() != obj.getClass()) return false; //checks for if the object's class is equal to the current class
        ChessBoard thatBoard = (ChessBoard) obj; //sets a new variable move equal to the object and casts object to a ChessMove Object.
        for (int r = 1; r < 9; r++) {
            for (int c = 1; c < 9; c++) {
                var p = new ChessPosition(r,c);
                var thisPiece = getPiece(p);
                var thatPiece = thatBoard.getPiece(p);
                if(thisPiece == null && thatPiece == null || (thisPiece.equals(thatPiece))){
                    // the pieces are equal, so continue.
                }else{
                    //the pieces are not equal, so return false
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public int hashCode() {
        int runningHash = 0;
        for (int r = 1; r < 9; r++) {
            for (int c = 1; c < 9; c++) {
                var p = new ChessPosition(r,c);
                var thisPiece = getPiece(p);
                runningHash += 13*thisPiece.hashCode();
            }
        }
        return runningHash;
    }
    public void executeMove(ChessMove move){
        //assuming that the move is valid and won't result in check, here's where the actual moving takes place.

    }
    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece){
        _board[position.getRow()][position.getColumn()] = piece;
    }
    public void removePiece(ChessPosition position){
        _board[position.getRow()][position.getColumn()] = null; //resets the pointer to null.
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return _board[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //add the black and white pawns, group 1:
        var white = ChessGame.TeamColor.WHITE;
        var black = ChessGame.TeamColor.BLACK;
        var bishop = ChessPiece.PieceType.BISHOP;
        var rook = ChessPiece.PieceType.ROOK;
        var knight = ChessPiece.PieceType.KNIGHT;
        var queen = ChessPiece.PieceType.QUEEN;
        var king = ChessPiece.PieceType.KING;
        ArrayList<ChessPiece.PieceType> group1 = new ArrayList<>();
        group1.add(rook);
        group1.add(knight);
        group1.add(bishop);
        group1.add(queen);
        group1.add(king);
        group1.add(bishop);
        group1.add(knight);
        group1.add(rook);
        var blackPawn = new ChessPiece(black,ChessPiece.PieceType.PAWN);
        var whitePawn = new ChessPiece(white,ChessPiece.PieceType.PAWN);
        for (int i = 1; i <9; i++) {
            ChessPosition bp = new ChessPosition(7,i);
            ChessPosition wp = new ChessPosition(2,i);
            addPiece(bp,blackPawn);
            addPiece(wp,whitePawn);
            ChessPosition topRow = new ChessPosition(8,i);
            ChessPosition bottomRow = new ChessPosition(1,i);
            var blackTopPiece = new ChessPiece(black, group1.get(i-1));
            var whiteTopPiece = new ChessPiece(white,group1.get(i-1));
            addPiece(topRow,blackTopPiece);
            addPiece(bottomRow,whiteTopPiece);
        }
        //now go over the for loop for both white and black.


    }
}
