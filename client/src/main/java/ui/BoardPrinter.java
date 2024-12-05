package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

public class BoardPrinter {

    public String printBlackGame(ChessGame chessGame) {
        //first print the game from the perspective of the black team,
        var board = chessGame.getBoard();
        StringBuilder gameString = new StringBuilder();
        for (int r = 0; r < 10; r++) {
            if (r == 0 || r == 9) {
                gameString.append(topBottomBlackPerspectiveString());
            } else {
                gameString.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK +
                        String.format(" %d ", r));
                if (r % 2 != 0) {
                    gameString.append(printBlackFirstRow(r, board, -1));
                } else {
                    gameString.append(printWhiteFirstRow(r, board, -1));
                }
                gameString.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK +
                        String.format(" %d ", r) + EscapeSequences.RESET_BG_COLOR + "\n");
            }
        }
        return gameString.toString();
    }

    public String printWhiteGame(ChessGame chessGame) {
        //first print the game from the perspective of the white team,
        var board = chessGame.getBoard();
        StringBuilder gameString = new StringBuilder();
        for (int r = 9; r >= 0; r--) {
            if (r == 0 || r == 9) {
                gameString.append(topBottomWhitePerspective());
            } else {
                gameString.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK +
                        String.format(" %d ", r));
                if (r % 2 == 0) { //if row is even
                    gameString.append(printWhiteFirstRow(r, board, 1));
                } else {
                    gameString.append(printBlackFirstRow(r, board, 1));
                }
                gameString.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK +
                        String.format(" %d ", r) + EscapeSequences.RESET_BG_COLOR + "\n");
            }
        }
        return gameString.toString();
    }

    public String printWhiteFirstRow(int r, ChessBoard board, int direction) {
        StringBuilder row = new StringBuilder();
        if (direction > 0) { //direction is positive, so we go positive.
            for (int c = 1; c < 9; c++) {
                secondRowPrintHelper(r, board, row, c);
            }
        } else {
            for (int c = 8; c >= 1; c--) {
                secondRowPrintHelper(r, board, row, c);
            }
        }
        return row.toString();
    }

    private void secondRowPrintHelper(int r, ChessBoard board, StringBuilder row, int c) {
        if (c % 2 == 0) {
            row.append(EscapeSequences.SET_BG_COLOR_BLACK + " ");
            row.append(stringizeChessPiece(r, c, board));
            row.append(" ");
        } else {//column number is odd,
            row.append(EscapeSequences.SET_BG_COLOR_WHITE + " ");
            row.append(stringizeChessPiece(r, c, board));
            row.append(" ");
        }
    }

    public String printBlackFirstRow(int r, ChessBoard board, int direction) {
        StringBuilder row = new StringBuilder();
        if (direction > 0) {
            for (int c = 1; c < 9; c++) {
                printRowsHelper(r, board, row, c);
            }
        } else {
            for (int c = 8; c >= 1; c--) {
                printRowsHelper(r, board, row, c);
            }
        }
        return row.toString();
    }

    private void printRowsHelper(int r, ChessBoard board, StringBuilder row, int c) {
        if (c % 2 != 0) {
            row.append(EscapeSequences.SET_BG_COLOR_BLACK + " ");
            row.append(stringizeChessPiece(r, c, board));
            row.append(" ");
        } else {//column number is odd,
            row.append(EscapeSequences.SET_BG_COLOR_WHITE + " ");
            row.append(stringizeChessPiece(r, c, board));
            row.append(" ");
        }
    }

    public String stringizeChessPiece(int r, int c, ChessBoard board) {
        ChessPosition p = new ChessPosition(r, c);
        var piece = board.getPiece(p);
        if (piece == null) {
            return " ";
        }
        var pieceType = piece.getPieceType();
        String pieceColor;
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            pieceColor = EscapeSequences.SET_TEXT_COLOR_RED;
        } else {
            pieceColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
        }
        switch (pieceType) {
            case ROOK -> {
                return pieceColor + "R";
            }
            case KNIGHT -> {
                return pieceColor + "N";
            }
            case BISHOP -> {
                return pieceColor + "B";
            }
            case QUEEN -> {
                return pieceColor + "Q";
            }
            case PAWN -> {
                return pieceColor + "P";
            }
            case KING -> {
                return pieceColor + "K";
            }
            default -> {
                return " ";
            }
        }
    }


    public String topBottomBlackPerspectiveString() {
        StringBuilder rowString = new StringBuilder();
        rowString.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ");
        for (char l = 'h'; l >= 'a'; l--) {
            rowString.append(" " + l + " ");
        }
        rowString.append("   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n");
        return rowString.toString();
    }

    public String topBottomWhitePerspective() {
        StringBuilder rowString = new StringBuilder();
        rowString.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK + "   ");
        for (char l = 'a'; l <= 'h'; l++) {
            rowString.append(" " + l + " ");
        }
        rowString.append("   " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n");
        return rowString.toString();
    }

}
