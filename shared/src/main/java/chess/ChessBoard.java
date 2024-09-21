package chess;

import java.util.ArrayList;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[9][9];
    public ChessBoard() {
        
    }
    @Override
    public void 

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece){
        squares[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()][position.getColumn()];
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
