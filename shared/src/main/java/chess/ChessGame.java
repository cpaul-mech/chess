package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();
    private TeamColor whoTurn;

    public ChessGame() {
        board.resetBoard();//starts the board off in a fresh state.
        whoTurn = TeamColor.WHITE; //every game starts off with white.
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return whoTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        whoTurn = team;
    }

    public void changeTurn() {
        //will swap the turns so that the not current team is in charge.
        if (whoTurn == TeamColor.WHITE) {
            whoTurn = TeamColor.BLACK;
        } else whoTurn = TeamColor.WHITE;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public ArrayList<ChessPosition> allPiecePositionsOfTeam(TeamColor color) {
        ArrayList<ChessPosition> pcPositions = new ArrayList<>();
        for (int r = 1; r < 9; r++) {
            for (int c = 1; c < 9; c++) {
                ChessPosition p = new ChessPosition(r, c);
                var pieceHere = board.getPiece(p);
                if (pieceHere != null && pieceHere.getTeamColor() == color) {
                    pcPositions.add(p);
                }
            }
        }
        return pcPositions;
    }

    public ArrayList<ChessMove> allValidMovesOfTeam(TeamColor color) {
        ArrayList<ChessPosition> allPositionsToCheck = allPiecePositionsOfTeam(color);
        ArrayList<ChessMove> allMoves = new ArrayList<>();
        for (ChessPosition position : allPositionsToCheck) {
            //iterate through all the elements of allPositionsToCheck
            Collection<ChessMove> vm = validMoves(position);
            for (ChessMove move : vm) {
                if (!allMoves.contains(move)) {
                    allMoves.add(move);
                }
            }
        }
        return allMoves;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        var pieceHere = board.getPiece(startPosition);
        if (pieceHere != null) {
            Collection<ChessMove> moveSet = pieceHere.pieceMoves(board, startPosition);
            //this will return all the moves available to the piece,
            //but it doesn't check for if the piece would place the king into check or leave the king in check
            //need to implement that.
            Collection<ChessMove> validMoveSet = new ArrayList<>();
            for (int i = 0; i < moveSet.size(); i++) {
                ChessMove m = ((ArrayList<ChessMove>) moveSet).get(i);
                if (doesNotEndangerKing(m)) {
                    validMoveSet.add(m);
                }//if the move would leave the king in danger afterward, then we also need to not include that move
            }
            return validMoveSet;
        } else return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //the move logic will go here, with access to the board, but the actual moving will be done by calling
        //execute move.
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (validMoves == null) {
            throw new InvalidMoveException("No piece is there!!");
        }
        if (validMoves.isEmpty()) {
            throw new InvalidMoveException("This piece is boxed in, cannot move anywhere.");
        }
        if (!moveCorrectColorPiece(move.getStartPosition())) {
            throw new InvalidMoveException("It is not this team's turn to move a piece.");
        }
        //need to iterate through the values of the validMoves returned, then see if the move is in them.
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("This move is not in validMoves");
        }

        if (!doesNotEndangerKing(move)) {
            throw new InvalidMoveException("This move would endanger the king!");
        }
        board.executeMove(move);
        changeTurn();

    }

    /**
     * @param p the Position of the piece that is trying to move.
     * @return true if it is that piece's turn to move now
     */
    public boolean moveCorrectColorPiece(ChessPosition p) {
        ChessPiece pc = board.getPiece(p);
        return pc.getTeamColor() == whoTurn;
    }

    /**
     * this method must ascertain whether the proposed move will leave the king in check
     *
     * @param move the ChessMove that may endanger the king
     * @return true if move will place king in check, and false if not.
     */
    public boolean doesNotEndangerKing(ChessMove move) {
        var copyBoard = new ChessBoard(board);
        var oldBoard = board;
        copyBoard.executeMove(move);
        setBoard(copyBoard);
        if (isInCheck(board.getPiece(move.getEndPosition()).getTeamColor())) {
            setBoard(oldBoard);
            return false;
        } else {
            setBoard(oldBoard);
            return true;
        }
        //at the end of this, java garbage collection should just remove the copyBoard object.
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);
        if (kingPosition == null) {
            return false; //if there is no king of that color, then that king cannot be put into check.
        }
        //now that we have found the king of the appropriate color, we need to check the other pieces and see if that piece
        //could move to take the king.
        if (teamColor == TeamColor.WHITE) {
            //white is at the bottom of the board, start searching in row 1.
            for (int r = 1; r < 9; r++) {
                for (int c = 1; c < 9; c++) {
                    //need to search for the king
                    ChessPosition p = new ChessPosition(r, c);
                    if (canKillKing(kingPosition, p)) {
                        return true;
                    }
                }
            }
        } else {
            for (int r = 8; r > 0; r--) {
                for (int c = 8; c > 0; c--) {
                    //need to search for the king
                    ChessPosition p = new ChessPosition(r, c);
                    if (canKillKing(kingPosition, p)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param kingPosition current Position of the king
     * @param killPosition position of the potential kingKiller
     * @return true if the specified killer can in fact kill the king.
     */
    public boolean canKillKing(ChessPosition kingPosition, ChessPosition killPosition) {
        var k = board.getPiece(killPosition);
        if (k == null) return false;
        ArrayList<ChessMove> killMoves = (ArrayList<ChessMove>) k.pieceMoves(board, killPosition); // I think I found the issue!!!
        if (killMoves == null) {
            return false; //a piece that doesn't exist cannot kill a king.
        }
        var kingColor = board.getPiece(kingPosition).getTeamColor();
        var killerColor = board.getPiece(killPosition).getTeamColor();
        //look for if pawns can kill the king.
        //when canKillKing is called on this
        if (kingColor != killerColor) {
            for (ChessMove m : killMoves) {
                if (m.getEndPosition().equals(kingPosition)) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * Finds the king of specified color and returns the position.
     *
     * @param teamColor the color of the king that you would like to find.
     * @return ChessPosition p, the position of the king, or null if no king was found.
     */
    public ChessPosition findKing(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE) {
            //white is at the bottom of the board, start searching in row 1.
            for (int r = 1; r < 9; r++) {
                for (int c = 1; c < 9; c++) {
                    //need to search for the king
                    ChessPosition p = new ChessPosition(r, c);
                    ChessPiece pc = board.getPiece(p);
                    if (pc != null && pc.getTeamColor() == teamColor && pc.getPieceType() == ChessPiece.PieceType.KING) {
                        return p;
                    }
                }
            }
        } else {
            for (int r = 8; r > 0; r--) {
                for (int c = 8; c > 0; c--) {
                    //need to search for the king
                    ChessPosition p = new ChessPosition(r, c);
                    ChessPiece pc = board.getPiece(p);
                    if (pc != null
                            && pc.getTeamColor() == TeamColor.BLACK
                            && pc.getPieceType() == ChessPiece.PieceType.KING) {
                        return p;
                    }
                }
            }
        }
        //there are some tests that require that a check for a king must not take place.
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        var allValidMoves = allValidMovesOfTeam(teamColor);
        return allValidMoves.isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //returns true
        var allValidMoves = allValidMovesOfTeam(teamColor);
        return allValidMoves.isEmpty() && !isInCheck(teamColor);
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
