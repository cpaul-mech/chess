package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard _board = new ChessBoard();
    private TeamColor _whoTurn;

    public ChessGame() {
        _board.resetBoard();//starts the board off in a fresh state.
        _whoTurn = TeamColor.WHITE; //every game starts off with white.
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return _whoTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        _whoTurn = team;
    }

    public void changeTurn(){
        //will swap the turns so that the not current team is in charge.
        if(_whoTurn == TeamColor.WHITE){
            _whoTurn = TeamColor.BLACK;
        }else _whoTurn = TeamColor.WHITE;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        var pieceHere = _board.getPiece(startPosition);
        if(pieceHere != null){
            return pieceHere.pieceMoves(_board,startPosition);
        }else return null;
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
        //TODO: enforce who's turn it is in whether the move is valid!!
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        //need to iterate through the values of the validMoves returned, then see if the move is in them.
        for (int i = 0; i < validMoves.size(); i++) {
            if(!validMoves.contains(move)){
                throw new InvalidMoveException("This move is not in validMoves");
            }
        }
        _board.executeMove(move);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
//        ChessPosition kingPosition = findKing(teamColor);
//        //now that we have found the king of the appropriate color, we need to check the other pieces and see if that piece
//        //could move to take the king.
//        if(teamColor == TeamColor.WHITE){
//            //white is at the bottom of the baord, start searching in row 1.
//            for (int r = 1; r < 9; r++) {
//                for (int c = 0; c < 9; c++) {
//                    //need to search for the king
//                    ChessPosition p = new ChessPosition(r,c);
//                    ChessPiece pc = _board.getPiece(p);
//                    if(pc.getTeamColor() == teamColor && pc.getPieceType() == ChessPiece.PieceType.KING){
//                        return p;
//                    }
//                }
//            }
//        }else {
//            for (int r = 9; r > 1; r--) {
//                for (int c = 9; c > 1; c--) {
//                    //need to search for the king
//                    ChessPosition p = new ChessPosition(r,c);
//                    ChessPiece pc = _board.getPiece(p);
//                    if(pc.getTeamColor() == TeamColor.WHITE && pc.getPieceType() == ChessPiece.PieceType.KING){
//                        return p;
//                    }
//                }
//            }
//        }
        return true;
    }

    public ChessPosition findKing(TeamColor teamColor){
        if(teamColor == TeamColor.WHITE){
            //white is at the bottom of the baord, start searching in row 1.
            for (int r = 1; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                 //need to search for the king
                 ChessPosition p = new ChessPosition(r,c);
                 ChessPiece pc = _board.getPiece(p);
                 if(pc.getTeamColor() == teamColor && pc.getPieceType() == ChessPiece.PieceType.KING){
                     return p;
                 }
                }
            }
        }else {
            for (int r = 9; r > 1; r--) {
                for (int c = 9; c > 1; c--) {
                    //need to search for the king
                    ChessPosition p = new ChessPosition(r,c);
                    ChessPiece pc = _board.getPiece(p);
                    if(pc.getTeamColor() == TeamColor.WHITE && pc.getPieceType() == ChessPiece.PieceType.KING){
                        return p;
                    }
                }
            }
        }
        throw new RuntimeException("The game has no king and should have ended.");
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        _board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return _board;
    }
}
