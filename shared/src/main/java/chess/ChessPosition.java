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
        return "Pos:["+ _row + "," + _col+"]";
    }
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true; //checks for memory address equals
        if(obj instanceof ChessPosition) {
            ChessPosition p = (ChessPosition)obj;
            if(this._row == p._row && this._col == p._col) return true;
        }
        return false;
    }
    @Override
    public int hashCode() {
        return 31 * _row + 61 *  _col;
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
