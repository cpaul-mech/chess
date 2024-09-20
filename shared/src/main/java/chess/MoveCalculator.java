package chess;

import java.util.ArrayList;

public class MoveCalculator {
    private final ChessPiece.PieceType pieceType;
    private ArrayList<ChessMove> _moveset;
    private final ChessPosition initial_position;
    private int _initial_row;
    private int _initial_col;
    private ChessBoard board;
    private ChessGame.TeamColor _teamColor;
    public MoveCalculator(ChessPiece.PieceType type, ArrayList<ChessMove> moveset, ChessPosition initial_position, ChessBoard board) {
        pieceType = type;
        _moveset = moveset;
        this.initial_position = initial_position;
        this.board = board;
        _initial_row = initial_position.getRow();
        _initial_col = initial_position.getColumn();
        _teamColor = board.getPiece(initial_position).getTeamColor();
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
        ChessPosition nextPosition;
        ChessPosition extraPositon = null;

        boolean extraMove = false;
        if(_initial_row == 2 || _initial_row == 7) extraMove = true; //flag it if the pawn can move again.
        if (_teamColor ==  ChessGame.TeamColor.WHITE){
            nextPosition = new ChessPosition(_initial_row+1, _initial_col);
            if(extraMove){extraPositon = new ChessPosition(_initial_row+2, _initial_col);}
        }
        else{
            nextPosition = new ChessPosition(_initial_row-1, _initial_col);
            if(extraMove){extraPositon = new ChessPosition(_initial_row-2, _initial_col);}
        }
        if(board.getPiece(nextPosition) == null) {
            var nextMove = new ChessMove(initial_position, nextPosition, null);
            _moveset.add(nextMove);
        }
        if(extraMove){
            if(board.getPiece(extraPositon) == null)
            {
                var nextMove = new ChessMove(initial_position, extraPositon, null);
                _moveset.add(nextMove);
            }
        }

        else return;

    }
    private void pawnTakePiece() {
        //Check for pieces on the diagonal, so up one row, and +-1 collumn
        //there is a division though in which side of the board we are on, Black is up top and White is on bottom.
        ChessPosition diagL;
        ChessPosition diagR;
        if(_teamColor == ChessGame.TeamColor.WHITE){
            //diagonals are upLeft and upRight
            diagL = new ChessPosition(_initial_row-1, _initial_col-1);
            diagR = new ChessPosition(_initial_row-1, _initial_col+1);
        }else{
            //black team, diagonals are downLeft and downRight
            diagL = new ChessPosition(_initial_row+1, _initial_col-1);
            diagR = new ChessPosition(_initial_row+1, _initial_col+1);
        }
        var LeftDiagPiece = board.getPiece(diagL);
        var RightDiagPiece = board.getPiece(diagR);
        //if either of these are not null, then add that one to the _moveset
        if(LeftDiagPiece != null) {
            var nextMove = new ChessMove(initial_position, diagL, null);
            _moveset.add(nextMove);
        }
    }
}
