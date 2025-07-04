package br.edu.dio.sudokuproject;

import lombok.Getter;

import java.util.Objects;

class Coordinate {


    @Getter
    private final Integer row;
    @Getter
    private final Integer col;
	private Cell associatedCell;

    /**
     * Constructs a new Coordinate.
     * @param row The row index (0-8).
     * @param col The column index (0-8).
     */
    public Coordinate(Integer row, Integer col) {
        this.row = row;
        this.col = col;
    }


    public void setCell(Cell cell){
		this.associatedCell = cell;
	}

    /**
     * Overrides equals for correct Map key behavior.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        boolean b = getRow() == that.getRow() && getCol() == that.getCol();
        return b;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }


    public String toString(){
        return String.format("(%d, %d)", row, col);
    }
}