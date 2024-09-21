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
        int[] upRightL = {2,1};
        int[] upLeftL = {2,-1};
        int[] leftUpL = {1,-2};
        int[] leftDownL = {-1,-2};
        int[] downLeftL = {-2,-1};
        int[] downRightL = {-2,1};
        int[] rightDownL = {-1,2};
        int[] rightUpL = {1,2};
        int limit = 1;
        move_straight_line(_initial_row+upRightL[0],_initial_col+upRightL[1],limit,upRightL);
        move_straight_line(_initial_row+upLeftL[0],_initial_col+upLeftL[1],limit,upLeftL);
        move_straight_line(_initial_row+leftUpL[0],_initial_col+leftUpL[1],limit,leftUpL);
        move_straight_line(_initial_row+leftDownL[0],_initial_col+leftDownL[1],limit,leftDownL);
        move_straight_line(_initial_row+downLeftL[0],_initial_col+downLeftL[1],limit,downLeftL);
        move_straight_line(_initial_row+downRightL[0],_initial_col+downRightL[1],limit,downRightL);
        move_straight_line(_initial_row+rightDownL[0],_initial_col+rightDownL[1],limit,rightDownL);
        move_straight_line(_initial_row+rightUpL[0],_initial_col+rightUpL[1],limit,rightUpL);
    }

    private void queenMoves() {
        int[] up = {1,0};
        int[] down = {-1, 0};
        int[] left = {0, -1};
        int[] right = {0, +1};
        int[] upRight = {1,1};
        int[] upLeft = {1,-1};
        int[] downRight = {-1,1};
        int[] downLeft = {-1,-1};
        int limit = 9;
        move_straight_line(_initial_row+1, _initial_col,limit,up);
        move_straight_line(_initial_row-1,_initial_col,limit,down);
        move_straight_line(_initial_row,_initial_col-1,limit,left);
        move_straight_line(_initial_row,_initial_col+1,limit,right);
        move_straight_line(_initial_row+1,_initial_col+1,limit,upRight);
        move_straight_line(_initial_row+1,_initial_col-1,limit,upLeft);
        move_straight_line(_initial_row-1,_initial_col+1,limit,downRight);
        move_straight_line(_initial_row-1,_initial_col-1,limit,downLeft);
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
        int[] upRight = {1,1};
        int[] upLeft = {1,-1};
        int[] downRight = {-1,1};
        int[] downLeft = {-1,-1};
        move_straight_line(_initial_row+1,_initial_col+1,9,upRight);
        move_straight_line(_initial_row+1,_initial_col-1,9,upLeft);
        move_straight_line(_initial_row-1,_initial_col+1,9,downRight);
        move_straight_line(_initial_row-1,_initial_col-1,9,downLeft);

    }

    private void kingMoves() {
        int[] up = {1,0};
        int[] down = {-1, 0};
        int[] left = {0, -1};
        int[] right = {0, +1};
        int[] upRight = {1,1};
        int[] upLeft = {1,-1};
        int[] downRight = {-1,1};
        int[] downLeft = {-1,-1};
        int limit = 1;
        move_straight_line(_initial_row+1, _initial_col,limit,up);
        move_straight_line(_initial_row-1,_initial_col,limit,down);
        move_straight_line(_initial_row,_initial_col-1,limit,left);
        move_straight_line(_initial_row,_initial_col+1,limit,right);
        move_straight_line(_initial_row+1,_initial_col+1,limit,upRight);
        move_straight_line(_initial_row+1,_initial_col-1,limit,upLeft);
        move_straight_line(_initial_row-1,_initial_col+1,limit,downRight);
        move_straight_line(_initial_row-1,_initial_col-1,limit,downLeft);
    }

    private void pawnMoves() {
        ChessPosition nextPosition;
        ChessPosition extraPositon = null;
        var promoPiece = ChessPiece.PieceType.QUEEN;

        boolean extraMove = (_initial_row == 2 && _teamColor == ChessGame.TeamColor.WHITE)
                || (_initial_row == 7 && _teamColor == ChessGame.TeamColor.BLACK);

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
            ChessMove nextMove = null;
            if(nextPosition.getRow() == 8 || nextPosition.getRow()==1){
                for(ChessPiece.PieceType piecetype1 : ChessPiece.PieceType.values()){
                    if(piecetype1 != ChessPiece.PieceType.PAWN && piecetype1 != ChessPiece.PieceType.KING){
                    nextMove = new ChessMove(initial_position, nextPosition, piecetype1);
                    _moveset.add(nextMove);
                    }
                }
            }
            else {
                nextMove = new ChessMove(initial_position, nextPosition, null);
                _moveset.add(nextMove);
            }

        }
        if(extraMove){
            ChessMove nextMove = null;
            var extraPiece = board.getPiece(extraPositon);
            if(board.getPiece(extraPositon) == null && board.getPiece(nextPosition) ==null)
            {
                if(nextPosition.getRow() == 8 || nextPosition.getRow()==1){
                    for(ChessPiece.PieceType piecetype1 : ChessPiece.PieceType.values()){
                        if(piecetype1 != ChessPiece.PieceType.PAWN && piecetype1 != ChessPiece.PieceType.KING){
                            nextMove = new ChessMove(initial_position, nextPosition, piecetype1);
                            _moveset.add(nextMove);
                        }
                    }
                }
                else{nextMove = new ChessMove(initial_position, extraPositon, null);
                    _moveset.add(nextMove);
                }

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
        var promoPiece = ChessPiece.PieceType.QUEEN;
        if(_teamColor == ChessGame.TeamColor.WHITE){
            //diagonals are upLeft and upRight
            diagL = new ChessPosition(_initial_row+1, _initial_col-1);
            diagR = new ChessPosition(_initial_row+1, _initial_col+1);
        }else{
            //black team, diagonals are downLeft and downRight
            diagL = new ChessPosition(_initial_row-1, _initial_col-1);
            diagR = new ChessPosition(_initial_row-1, _initial_col+1);
        }
        var LeftDiagPiece = board.getPiece(diagL);
        var RightDiagPiece = board.getPiece(diagR);
        //if either of these are not null, then add that one to the _moveset
        ChessMove nextMove;
        if(LeftDiagPiece != null) {
            if(diagL.getRow() == 8 || diagL.getRow()==1){
                for(ChessPiece.PieceType piecetype1 : ChessPiece.PieceType.values()){
                    if(piecetype1 != ChessPiece.PieceType.PAWN && piecetype1 != ChessPiece.PieceType.KING){
                        nextMove = new ChessMove(initial_position, diagL, piecetype1);
                        _moveset.add(nextMove);
                    }
                }
            }
            else {
                nextMove = new ChessMove(initial_position, diagL, null);
                _moveset.add(nextMove);
            }
        }else if(RightDiagPiece != null){
            if(diagR.getRow() == 8 || diagR.getRow()==1){
                for(ChessPiece.PieceType piecetype1 : ChessPiece.PieceType.values()){
                    if(piecetype1 != ChessPiece.PieceType.PAWN && piecetype1 != ChessPiece.PieceType.KING){
                        nextMove = new ChessMove(initial_position, diagR, piecetype1);
                        _moveset.add(nextMove);
                    }
                }
            }
            else {
                nextMove = new ChessMove(initial_position, diagR, null);
                _moveset.add(nextMove);
            }
        }
    }
//
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
