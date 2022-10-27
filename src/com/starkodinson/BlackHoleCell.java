package com.starkodinson;

import java.util.Objects;

public class BlackHoleCell extends Cell {
    private int tpr;
    private int tpc;

    public BlackHoleCell(int row, int col, int tprow, int tpcol) {
        super(row, col);
        this.tpr = tprow;
        this.tpc = tpcol;
    }

    public int[] getTPCoords()
    {
        return new int[]{tpr, tpc};
    }

    public void updateTPCoords(int ntpr, int ntpc)
    {
        tpr = ntpr;
        tpc = ntpc;
    }
}
