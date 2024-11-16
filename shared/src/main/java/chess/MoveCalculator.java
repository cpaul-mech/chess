package chess;

import java.util.ArrayList;
import java.util.Arrays;

public class MoveCalculator {
    private final ChessBoard board;
    private final ChessPiece piece;
    private final ChessPosition startPosition;
    private final int sRow;
    private final int sCol;
    private final ArrayList<ChessMove> moveSet = new ArrayList<>();
    private final ChessPiece.PieceType type;
    private final ChessGame.TeamColor color;
    private final int[] upLeft = {1, -1};
    private final int[] upRight = {1, 1};
    private final int[] downLeft = {-1, -1};
    private final int[] downRight = {-1, 1};

    public MoveCalculator(ChessBoard board, ChessPiece piece, ChessPosition position) {
        this.board = board;
        this.piece = piece;
        startPosition = position;
        sRow = position.getRow();
        sCol = position.getColumn();
        type = piece.getPieceType();
        color = piece.getTeamColor();
    }

    private void moveCalculatorCaller(ArrayList<int[]> movesToCall, int limit, int movesToCallLimit) {
        for (int i = 0; i < movesToCallLimit; i++) {
            moveStraightLineHelper(movesToCall.get(i), limit);
        }

    }

    public ArrayList<ChessMove> calculateMoveSet() {
        int[] up = {1, 0};
        int[] down = {-1, 0};
        int[] left = {0, -1};
        int[] right = {0, 1};
        ArrayList<int[]> movesToCall = new ArrayList<>(Arrays.asList(up, down, left, right, upLeft, upRight, downLeft, downRight));
        int[][] knightDirections = {{2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}};
        int limit;
        switch (type) {
            case ROOK:
                limit = 9;
                moveCalculatorCaller(movesToCall, limit, 4);
                break;
            case KING:
                limit = 1;
                moveCalculatorCaller(movesToCall, limit, 8);
                break;
            case QUEEN:
                limit = 9;
                moveCalculatorCaller(movesToCall, limit, 8);
                break;
            case PAWN:
                //oh boy, here we go!
                switch (color) {
                    case BLACK:
                        //BLACK PAWNS MOVE DOWN
                        moveStraightLineHelper(down, 1);
                        //now check for diagonal move possibilities
                        moveStraightLineHelper(downLeft, 1);
                        moveStraightLineHelper(downRight, 1);
                        //now check for double movement possibility
                        if (sRow == 7 && board.getPiece(new ChessPosition(6, sCol)) == null) {
                            int[] dubDown = {-2, 0};
                            moveStraightLineHelper(dubDown, 1);
                        }
                        break;
                    case WHITE:
                        //WHITE PAWNS MOVE UP, PROMOTED AT ROW 8
                        moveStraightLineHelper(up, 1);
                        moveStraightLineHelper(upRight, 1);
                        moveStraightLineHelper(upLeft, 1);
                        if (sRow == 2 && board.getPiece(new ChessPosition(3, sCol)) == null) {
                            int[] dubUp = {2, 0};
                            moveStraightLineHelper(dubUp, 1);

                        }
                        break;
                    default:
                        break;
                }

                break;
            case BISHOP:
                limit = 9;
                moveStraightLineHelper(upRight, limit);
                moveStraightLineHelper(upLeft, limit);
                moveStraightLineHelper(downRight, limit);
                moveStraightLineHelper(downLeft, limit);
                break;
            case KNIGHT:
                for (int[] knightDirection : knightDirections) {
                    moveStraightLineHelper(knightDirection, 1);
                }
                break;
            default:
                break;
        }
        return moveSet;
    }

    private void moveStraightLineHelper(int[] direction, int limit) {
        //this function is called before moveStraightLine so that I don't mess up.
        moveStraightLine(sRow + direction[0], sCol + direction[1], direction, limit);
    }

    private void moveStraightLine(int row, int col, int[] direction, int limit) {
        //this function enables the piece to move in whatever direction it is specified to have.
        if ((row >= 1 && row <= 8 && col >= 1 && col <= 8) && limit >= 1) {
            ChessPosition thisPosition = new ChessPosition(row, col);
            ChessMove propMove = pawnPromoChecker(thisPosition, direction);
            if (otherPieceHere(thisPosition)) {
                var otherPiece = board.getPiece(thisPosition);
                if (isEnemyPiece(otherPiece) && pawnMoveDiagChecker(direction)) {
                    //if they're an enemy, then put in the current chessMove and halt the function here.
                    moveSet.add(propMove);
                }
                return;
            } else if (type == ChessPiece.PieceType.PAWN) {
                if (!pawnMoveDiagChecker(direction)) {
                    moveSet.add(propMove);
                }
            } else {
                moveSet.add(propMove);
            }

            //nobody else is here, lets put the piece in and move on!
            moveStraightLine(row + direction[0], col + direction[1], direction, limit - 1);
        }
    }

    private boolean pawnMoveDiagChecker(int[] direction) {
        if (type == ChessPiece.PieceType.PAWN) {
            return Arrays.equals(direction, upLeft) || Arrays.equals(direction, upRight) ||
                    Arrays.equals(direction, downRight) || Arrays.equals(direction, downLeft);
        } else {
            return true;
        }
    }

    private ChessMove pawnPromoChecker(ChessPosition p, int[] direction) {
        if (type == ChessPiece.PieceType.PAWN) {
            ArrayList<ChessPiece.PieceType> pawnPromoOptions = new ArrayList<>();
            pawnPromoOptions.add(ChessPiece.PieceType.ROOK);
            pawnPromoOptions.add(ChessPiece.PieceType.KNIGHT);
            pawnPromoOptions.add(ChessPiece.PieceType.BISHOP);
            pawnPromoOptions.add(ChessPiece.PieceType.QUEEN);
            if (color == ChessGame.TeamColor.WHITE) {
                return pawnPromoCheckerHelper(p, 8, pawnPromoOptions, direction);
            } else {
                return pawnPromoCheckerHelper(p, 1, pawnPromoOptions, direction);
            }

        } else {
            return new ChessMove(startPosition, p, null);
        }
    }

    private ChessMove pawnPromoCheckerHelper(ChessPosition p, int x, ArrayList<ChessPiece.PieceType> pawnPromoOptions, int[] direction) {
        if ((p.getRow() == x) &&
                ((pawnMoveDiagChecker(direction) && isEnemyPiece(board.getPiece(p)))
                        || (!pawnMoveDiagChecker(direction)) && !isEnemyPiece(board.getPiece(p)))) {
            for (int i = 0; i < pawnPromoOptions.size() - 1; i++) {
                moveSet.add(new ChessMove(startPosition, p, pawnPromoOptions.get(i)));
            }
            return new ChessMove(startPosition, p, pawnPromoOptions.get(3));
        } else {
            return new ChessMove(startPosition, p, null);
        }
    }

    private boolean isEnemyPiece(ChessPiece other) {
        if (piece != null && other != null) {
            return piece.getTeamColor().ordinal() != other.getTeamColor().ordinal();
        } else {
            return false;
        }
    }

    private boolean otherPieceHere(ChessPosition p) {
        return board.getPiece(p) != null;
    }


}
