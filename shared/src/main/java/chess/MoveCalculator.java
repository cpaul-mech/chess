package chess;

import java.util.ArrayList;

public class MoveCalculator {
    private final ChessPiece.PieceType pieceType;
    private final ArrayList<ChessMove> _moveset;
    private final ChessPosition initial_position;
    private final int _initial_row;
    private final int _initial_col;
    private final ChessBoard board;
    private final ChessGame.TeamColor _teamColor;
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
        //can go up, down, left, right.
        int[] up = {1,0};
        int[] down = {-1, 0};
        int[] left = {0, -1};
        int[] right = {0, +1};
        var last = new ChessPosition(_initial_row,_initial_col);
        move_straight_line(_initial_row+1, _initial_col,9,up);
        move_straight_line(_initial_row-1,_initial_col,9,down);
        move_straight_line(_initial_row,_initial_col-1,9,left);
        move_straight_line(_initial_row,_initial_col+1,9,right);

    }

    private void bishopMoves() {

    }

    private void kingMoves() {

    }

    private void pawnMoves() {
        ChessPosition nextPosition;
        ChessPosition extraPositon = null;

        boolean extraMove = _initial_row == 2 || _initial_row == 7;
        //flag it if the pawn can move again.
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
        pawnTakePiece();
        //don't need a return statement!!
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
        }else if(RightDiagPiece != null){
            var nextMove = new ChessMove(initial_position, diagR, null);
            _moveset.add(nextMove);
        }
    }
//    private void pawnPromotionCheck(ChessPosition end, boolean white = true;){
//        //the job of this function is to check for if the place that the pawn is moving will place the pawn in the end zone.
//
//    }
    public void move_straight_line(int row, int col, int limit, int[] directionArray){
        //direction indicates the movement that the recursive function will integer array with two items.
        ChessPosition thisPosition = new ChessPosition(row, col);
        if(limit == 0){
            return;
        }
        if(row>8||row<=0||col>8||col<=0){
            //outside the range of the board.
            return;
        }
        if(pieceHere(thisPosition)){
            if(board.getPiece(thisPosition).getTeamColor().ordinal() == _teamColor.ordinal()){
                //if they're on the same team
                return;
            }
            var validChessMove = new ChessMove(initial_position, thisPosition,null);
            _moveset.add(validChessMove);
            return;
        }
        var validChessMove = new ChessMove(initial_position, thisPosition,null);
        _moveset.add(validChessMove);
        move_straight_line(row + directionArray[0], col + directionArray[1], limit - 1, directionArray);
    }

    public boolean pieceHere(ChessPosition position){
        //if piece here
        var piece_here = board.getPiece(position);
        if(piece_here == null){
            return false;
        }
        return true;
    }
}
