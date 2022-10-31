package com.starkodinson;

import java.util.Objects;

public class Cell {
    protected int row, col;
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public int getRow() {return row;}
    public int getCol() {return col;}
    public String toString() {return "("+row+","+col+")";}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}