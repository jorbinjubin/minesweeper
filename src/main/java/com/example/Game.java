package com.example;
// import com.example.Cell;
import java.util.*;

public class Game {
    Cell[][] board;
    int h,w;
    boolean won;
    boolean end;
    int flags;

    public Game(int width, int height) {
        board = new Cell[height][width];
        h = height;
        w = width;
        for(int i = 0; i < h; i++) {
            for(int j = 0; j < w; j++) {
                board[i][j] = new Cell(false);
            }
        }
    }

    public void init(int x, int y) {
        Random rand = new Random();
        int numMines = (int)(h * w * 0.2);
        flags = numMines;

        // Place Mines
        while(numMines >= 0) {
            int mX = rand.nextInt(h);
            int mY = rand.nextInt(h);

            // Place the required number of mines in unique squares and not the starting square
            if(!board[mY][mX].isMine && (x != mX && y != mY)) {
                board[mY][mX].setMine();
                numMines--;
            }
        }
        
        // Populate Adjacency
        for(int i = 0; i < h; i++) {
            for(int j = 0; j < w; j++) {
                board[i][j].setNumAdjacent(getAdj(i, j));
            }
        }
        // BFS Starting Area
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        Queue<int[]> dq = new LinkedList<>();
        while(!dq.isEmpty()) {
            int[] front = dq.poll();
            int br = front[0], bc = front[1];
            for(int[] i : dirs) {
                int b2r = br + i[0];
                int b2c = bc + i[1];

                if(b2r >= 0 && b2r < h && b2c >= 0 && b2c < w && board[b2r][b2c].getCount() == 0) {
                    board[b2r][b2c].uncover();
                    dq.add(new int[]{b2r, b2c});
                }
            }
        }
    }

    public char[][] getBoard() {
        char[][] ret = new char[h][w];
        for(int i = 0; i < h; i++) {
            for(int j = 0; j < w; j++) {
                Cell c = board[i][j];
                ret[i][j] = c.getIsUncovered() ? (c.getCount() + "").charAt(0) : '.';
//                ret[i][j] = c.isMine() ? 'B' : (c.getCount() + "").charAt(0);
            }
        }

        return ret;
    }

    public int getAdj(int mX, int mY) {
        int[] dirs = { -1, 0, 1 };
        int cnt = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int nX = mX + dirs[i];
                int nY = mY + dirs[j];
                if (my + dirs[i] >= 0 && my + dirs[i] < h && mx + dirs[j] >= 0 && mx + dirs[j] < w) {
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
