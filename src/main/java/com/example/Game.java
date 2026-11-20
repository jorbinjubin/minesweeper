package com.example;

// import com.example.Cell;
import java.util.*;

public class Game {
    Cell[][] board;
    int rows, cols;
    boolean won;
    boolean end;
    int flags;

    public Game(int numRows, int numCols) {
        board = new Cell[numRows][numCols];
        rows = numRows;
        cols = numCols;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = new Cell(false);
            }
        }
    }

    public void init(int r, int c) {
        Random rand = new Random();
        int numMines = (int) (rows * cols * 0.2);
        flags = numMines;

        // Place Mines
        while (numMines > 0) {
            int mr = rand.nextInt(rows);
            int mc = rand.nextInt(cols);

            // Place the required number of mines in unique squares and not the starting
            // square
            if (!board[mr][mc].isMine && (r != mr || c != mc)) {
                board[mr][mc].setMine();
                numMines--;
            }
        }

        // Populate Adjacency
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j].setNumAdjacent(getAdj(i, j));
            }
        }
        // BFS Starting Area
        int[][] dirs = { 
            { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, 
            { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 } 
        };
        Deque<int[]> dq = new ArrayDeque<>();
        dq.add(new int[] { r, c });
        while (!dq.isEmpty()) {
            int[] front = dq.pollLast();
            int br = front[0], bc = front[1];
            for (int[] i : dirs) {
                int b2r = br + i[0];
                int b2c = bc + i[1];

                if (b2r >= 0 && b2r < rows && b2c >= 0 && b2c < cols && board[b2r][b2c].getCount() == 0 && !board[b2r][b2c].getIsUncovered()) {
                    board[b2r][b2c].uncover();
                    dq.add(new int[] { b2r, b2c });
                }
            }
        }
        // TODO: Uncover the 8 cells around each of the initial BFS'd cells
    }

    public char[][] getBoard() {
        char[][] ret = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell c = board[i][j];
                ret[i][j] = c.getIsUncovered() ? (c.getCount() + "").charAt(0) : '.';
                // ret[i][j] = c.isMine() ? 'B' : (c.getCount() + "").charAt(0);
            }
        }

        return ret;
    }

    public int getAdj(int my, int mx) {
        int[] dirs = { -1, 0, 1 };
        int cnt = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int ny = my + dirs[i];
                int nx = mx + dirs[j];
                if (my + dirs[i] >= 0 && my + dirs[i] < rows && mx + dirs[j] >= 0 && mx + dirs[j] < cols) {
                    if (board[ny][nx].isMine())
                        cnt++;
                }
            }
        }
        return cnt;
    }

    public void uncover(int r, int c) {
        board[r][c].uncover();
    }
}
