package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int _row;
    private int _col;

    public ChessPosition(int row, int col) {
        this._row = row;
        this._col = col;
    }
    @Override
    public String toString() {
        return "Pos: ["+ _row + ", " + _col+"]";
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this._row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this._col;
    }
}
