package com.example;

public class Cell {
    public int numAdjacent;
    public boolean isFlag;
    public boolean isMine;
    public boolean isUncovered;

    public Cell(boolean isBomb) {
        this.isMine = isBomb;
        numAdjacent = 0;
        isFlag = false;
        isUncovered = false;
    }

    public int getCount() {
        return numAdjacent;
    }

    public void setNumAdjacent(int n) {
        numAdjacent = n;
    }

    public void uncover() {
        isUncovered = true;
    }

    public boolean getIsUncovered() {
        return isUncovered;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine() {
        isMine = true;
    }

    public boolean flag() {
        isFlag = !isFlag;
        return isFlag;
    }
}
