package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "Pos:[" + row + "," + col + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true; //checks for memory address equals
        if (obj instanceof ChessPosition) {
            ChessPosition p = (ChessPosition) obj;
            return this.row == p.row && this.col == p.col;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31 * row + 61 * col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }
}
