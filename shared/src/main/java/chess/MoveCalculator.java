package chess;

import java.util.ArrayList;

public class MoveCalculator {
    private final ChessPiece.PieceType pieceType;
    private ArrayList<ChessMove> _moveset;
    private final ChessPosition initial_position;
    private int _initial_row;
    private int _initial_col;
    private ChessBoard board;
    public MoveCalculator(ChessPiece.PieceType type, ArrayList<ChessMove> moveset, ChessPosition initial_position, ChessBoard board) {
        pieceType = type;
        _moveset = moveset;
        this.initial_position = initial_position;
        this.board = board;
        _initial_row = initial_position.getRow();
        _initial_col = initial_position.getColumn();
    }

    public ArrayList<ChessMove> calculate_moveset() {
        switch (pieceType) {
            case PAWN:
                pawnMoves();
                break;
            case KNIGHT:
                knightMoves();
                break;
            case BISHOP:
                bishopMoves();
                break;
            case ROOK:
                rookMoves();
                break;
            case QUEEN:
                queenMoves();
                break;
            case KING:
                kingMoves();
                break;
            default:
                throw new RuntimeException("Hit default case in MoveCalculator class");
        }
        return _moveset;
    }

    private void knightMoves() {
    }

    private void queenMoves() {
    }

    private void rookMoves() {

    }

    private void bishopMoves() {

    }

    private void kingMoves() {
    }

    private void pawnMoves() {
        var nextPosition = new ChessPosition(_initial_row+1, _initial_col);
        if(board.getPiece(nextPosition) == null) {
            var nextMove = new ChessMove(initial_position, nextPosition, pieceType);
            _moveset.add(nextMove);
        }
        else return;
        //pawn can move forward only for now.

    }
}
