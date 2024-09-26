package chess;

import java.util.ArrayList;
import java.util.Arrays;

public class MoveCalculator {
    private ChessBoard _board;
    private ChessPiece _piece;
    private ChessPosition _startPosition;
    private int _sRow;
    private int _sCol;
    private ArrayList<ChessMove> _moveSet = new ArrayList<>();
    private ChessPiece.PieceType _type;
    private ChessGame.TeamColor _color;

    public MoveCalculator(ChessBoard board, ChessPiece piece, ChessPosition position){
        _board = board;
        _piece = piece;
        _startPosition = position;
        _sRow = position.getRow();
        _sCol = position.getColumn();
        _type = piece.getPieceType();
        _color = piece.getTeamColor();
    }

    public ArrayList<ChessMove> calculateMoveSet(){
        int[] up = {1,0};
        int[] down = {-1,0};
        int[] left = {0,-1};
        int[] right = {0,1};
        int[] upLeft = {1,-1};
        int[] upRight = {1,1};
        int[] downLeft = {-1,-1};
        int[] downRight = {-1,1};
        int[][] knightDirections = {{2,1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2},{1,-2},{2,-1}};
        int limit;
        switch (_type){
            case ROOK:
                limit=9;
                moveStraightLineHelper(up,limit);
                moveStraightLineHelper(down,limit);
                moveStraightLineHelper(left,limit);
                moveStraightLineHelper(right,limit);
                break;
            case KING:
                limit=1;
                moveStraightLineHelper(up,limit);
                moveStraightLineHelper(down,limit);
                moveStraightLineHelper(left,limit);
                moveStraightLineHelper(right,limit);
                moveStraightLineHelper(upRight,limit);
                moveStraightLineHelper(upLeft,limit);
                moveStraightLineHelper(downRight,limit);
                moveStraightLineHelper(downLeft,limit);
                break;
            case QUEEN:
                limit=9;
                moveStraightLineHelper(up,limit);
                moveStraightLineHelper(down,limit);
                moveStraightLineHelper(left,limit);
                moveStraightLineHelper(right,limit);
                moveStraightLineHelper(upRight,limit);
                moveStraightLineHelper(upLeft,limit);
                moveStraightLineHelper(downRight,limit);
                moveStraightLineHelper(downLeft,limit);
                break;
            case PAWN:
                //oh boy, here we go!
                switch (_color){
                    case BLACK:
                        //BLACK PAWNS MOVE DOWN
                        moveStraightLineHelper(down,1);
                        //now check for diagonal move possibilities
                        moveStraightLineHelper(downLeft,1);
                        moveStraightLineHelper(downRight,1);
                        //now check for double movement possibility
                        if(_sRow==7 && _board.getPiece(new ChessPosition(6,_sCol))==null){
                            int[] dubDown = {-2,0};
                            moveStraightLineHelper(dubDown,1);
                        }
                        break;
                    case WHITE:
                        //WHITE PAWNS MOVE UP, PROMOTED AT ROW 8
                        moveStraightLineHelper(up,1);
                        moveStraightLineHelper(upRight,1);
                        moveStraightLineHelper(upLeft,1);
                        if(_sRow==2 && _board.getPiece(new ChessPosition(3,_sCol))==null){
                            int[] dubUp = {2,0};
                            moveStraightLineHelper(dubUp,1);

                        }
                        break;
                    default:
                        break;
                }

                break;
            case BISHOP:
                limit=9;
                moveStraightLineHelper(upRight,limit);
                moveStraightLineHelper(upLeft,limit);
                moveStraightLineHelper(downRight,limit);
                moveStraightLineHelper(downLeft,limit);
                break;
            case KNIGHT:
                for (int i = 0; i < knightDirections.length; i++) {
                    moveStraightLineHelper(knightDirections[i],1);
                }
                break;
            default:
                break;
        }
        return _moveSet;
    }

    private void moveStraightLineHelper(int[]direction, int limit){
        //this function is called before moveStraightLine so that I don't mess up.
        moveStraightLine(_sRow+direction[0],_sCol+direction[1],direction,limit);
    }

    private void moveStraightLine(int row, int col, int[] direction, int limit){
        //this function enables the piece to move in whatever direction it is specified to have.
        if(row<1||row>8||col<1||col>8) return;
        else if(limit<1) return;
        else{
            ChessPosition this_position = new ChessPosition(row,col);
            ChessMove propMove = pawnPromoChecker(this_position,direction);
            if(otherPieceHere(this_position)){
                var otherPiece = _board.getPiece(this_position);
                if(isEnemyPiece(otherPiece) && pawnMoveDiagChecker(direction)){
                    //if they're an enemy, then put in the current chessMove and halt the function here.
                    _moveSet.add(propMove);
                    return;
                }else{
                    return;
                }
            } else if(_type== ChessPiece.PieceType.PAWN) {
                if (!pawnMoveDiagChecker(direction)) {
                    _moveSet.add(propMove);
                }
            }else {
                _moveSet.add(propMove);
            }

            //nobody else is here, lets put the piece in and move on!
            moveStraightLine(row+direction[0],col+direction[1],direction,limit-1);
        }
    }
    private boolean pawnMoveDiagChecker(int[] direction){
        if(_type== ChessPiece.PieceType.PAWN) {
            int[] upLeft = {1, -1};
            int[] upRight = {1, 1};
            int[] downLeft = {-1, -1};
            int[] downRight = {-1, 1};
            if (Arrays.equals(direction, upLeft) || Arrays.equals(direction, upRight) ||
                    Arrays.equals(direction,downRight) || Arrays.equals(direction,downLeft)) {
                return true;
            } else return false;
        }else return true;
    }
    private ChessMove pawnPromoChecker(ChessPosition p,int[] direction){
        //checks if the pieceType is pawn, then only returns a different move if the piece is in the end zone, and there is an enemy and we move diagonally,
        //or if there is not an enemy and we're moving straight.
        if(_type == ChessPiece.PieceType.PAWN) {
            ArrayList<ChessPiece.PieceType> pawnPromoOptions = new ArrayList<>();
            pawnPromoOptions.add(ChessPiece.PieceType.ROOK);
            pawnPromoOptions.add(ChessPiece.PieceType.KNIGHT);
            pawnPromoOptions.add(ChessPiece.PieceType.BISHOP);
            pawnPromoOptions.add(ChessPiece.PieceType.QUEEN);
            if (_color == ChessGame.TeamColor.WHITE) {
                //white pawn, promotion happens at row 8!
                if (p.getRow() == 8 &&
                        ((pawnMoveDiagChecker(direction)&&isEnemyPiece(_board.getPiece(p)))
                                ||(!pawnMoveDiagChecker(direction))&&!isEnemyPiece(_board.getPiece(p)))) {
                    //we need to add in every possibility except the last one, so we can carry on normal function
                    for (int i = 0; i < pawnPromoOptions.size()-1; i++) {//loop executes 3 times, ending at 2
                        //stops before the last element so we can return it and carry on business as usual.
                        _moveSet.add(new ChessMove(_startPosition,p,pawnPromoOptions.get(i)));
                    }
                    return new ChessMove(_startPosition,p,pawnPromoOptions.get(3));
                }else return new ChessMove(_startPosition,p,null);
            }else {
                if (p.getRow() == 1 &&
                        ((pawnMoveDiagChecker(direction)&&isEnemyPiece(_board.getPiece(p)))
                                ||(!pawnMoveDiagChecker(direction))&&!isEnemyPiece(_board.getPiece(p)))) {
                    for (int i = 0; i < pawnPromoOptions.size()-1; i++) {//loop executes 3 times, ending at 2
                        //stops before the last element so we can return it and carry on business as usual.
                        _moveSet.add(new ChessMove(_startPosition,p,pawnPromoOptions.get(i)));
                    }
                    return new ChessMove(_startPosition,p,pawnPromoOptions.get(3));
                }else return new ChessMove(_startPosition,p,null);
            }

        }else {
            return new ChessMove(_startPosition,p,null);
        }
    }
    private boolean isEnemyPiece(ChessPiece other){
        if(_piece!=null && other != null){
            if(_piece.getTeamColor().ordinal() == other.getTeamColor().ordinal()){
                return false;
            }else return true;
        }else return false;

    }
    private boolean otherPieceHere(ChessPosition p){
        if (_board.getPiece(p) != null){
            return true;
        }else {
            return false;
        }
    }


}
