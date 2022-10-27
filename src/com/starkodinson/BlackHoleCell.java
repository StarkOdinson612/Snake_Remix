package com.starkodinson;

import java.util.Objects;

public class BlackHoleCell extends Cell {
    private BlackHoleCell other;

    public BlackHoleCell(int row, int col) {
        super(row, col);
    }

    public static void initializePair(BlackHoleCell cell, BlackHoleCell cell2)
    {
        cell.setOther(cell2);
        cell2.setOther(cell);
    }

    public void setOther(BlackHoleCell other) { this.other = other; }

    public int[] getTPCoords()
    {
        return new int[]{other.getCol(), other.getRow()};
    }

    public void setCoords(int col, int row) {
        super.row = row;
        super.col = col;
    }
}
