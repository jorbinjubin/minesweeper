import java.util.*;

public class Minesweeper {

    // Pure laziness right here
    static Random rand;
    static boolean[][] mines;
    static int r, c;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Minesweeper!");
        System.out.println("Try to uncover every cell except for the mines.");
        System.out.println(
                "Use commands in the format \'u11\' or \'f11\', where u stands for uncover and f stands for flag");
        System.out.println("The first number is the row and the second number is the column.");

        // Pure laziness right here
        int numMines = 30;
        c = 10;
        r = 10;
        // Display layer: keeps track of flags and reveals
        char[][] board = new char[r][c];

        // Flag layer: keep track of flags, seperate from reveals
        boolean[][] flags = new boolean[r][c];

        // Mine layer: keep track of mines
        mines = new boolean[r][c];

        // Fill board with '.'
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = '.';
            }
        }
        rand = new Random();

        // Place the required number of mines in unique positions
        while (numMines > 0) {
            int cX = rand.nextInt(board[0].length);
            int cY = rand.nextInt(board.length);
            if (!mines[cY][cX]) {
                mines[cY][cX] = true;
                numMines--;
            }
        }

        boolean exit = false;
        boolean win = false;
        int remFlags = 0;
        printBoard(board, flags);
        System.out.print("Enter your move: ");
        char[] cmd = sc.nextLine().toCharArray();
        int my = Integer.parseInt(cmd[1] + "");
        int mx = Integer.parseInt(cmd[2] + "");
        floodFill(mines, board, my, mx);
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (mines[i][j]) {
                    remFlags++;
                }
            }
        }

        while (!exit) {
            printBoard(board, flags);
            System.out.println("Remaining flags: " + remFlags);
            System.out.print("Enter your move: ");
            cmd = sc.nextLine().toCharArray();
            my = Integer.parseInt(cmd[1] + "");
            mx = Integer.parseInt(cmd[2] + "");

            switch (cmd[0]) {

                case 'u':
                    if (mines[my][mx]) {
                        // You Lose!
                        exit = true;
                        break;
                    } else {
                        // You don't lose!
                        board[my][mx] = uncover(my, mx);
                    }
                    break;
                case 'f':
                    // Flag Cell
                    if(flags[my][mx]) {
                        remFlags++;
                    } else {
                        remFlags--;
                    }
                    flags[my][mx] = !flags[my][mx];
                    break;
                case 'c':
                    // Cheat (for debugging, of course)
                    System.out.println("+-----------------------------------------------------------+");
                    printBoard(board, mines, true);
                    System.out.println("+-----------------------------------------------------------+");
                    break;
                default:
                    System.out.println("Enter a valid move (u for uncover, f for flag).");
            }
            if (checkBoard(mines, flags)) {
                win = true;
                exit = true;
            }
        }

        System.out.println("Game over!");
        if (win) {
            System.out.println("You won!");
        } else {
            System.out.println("You lost!");
            printBoard(board, flags, true);
        }
        sc.close();
    }

    /**
     * Print the board, including counts and flags
     * 
     * @param board The board to be printed
     * @param flags The relevant flags
     */
    public static void printBoard(char[][] board, boolean[][] flags) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != '.')
                    System.out.print(board[i][j] + " ");
                else if (flags[i][j]) {
                    System.out.print("^ ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Print the boards, only the bombs (for debugging)
     * 
     * @param board The board to be printed
     * @param mines The relevant mines
     * @param a     Overloading variable
     */
    public static void printBoard(char[][] board, boolean[][] mines, boolean a) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (mines[i][j]) {
                    System.out.print("B ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Verify if the board is solved (all mines are flagged)
     * 
     * @param mines The relevant mines
     * @return      If the board is solved
     */
    public static boolean checkBoard(boolean[][] mines, boolean[][] flags) {
        boolean won = true;
        for (int i = 0; i < mines.length; i++) {
            for (int j = 0; j < mines[0].length; j++) {
                if (mines[i][j] && !flags[i][j]) {
                    won = false;
                    break;
                }
            }
        }
        return won;
    }

    /**
     * Flood fill (BFS) the starting area of the board and reveal it (to make the game possible)
     * Filled cells are first changed to 'F', then all 'F' are uncovered and replaced by their respective number.
     * @param mines The relevant mines
     * @param board The relevant board
     * @param mr    The row of the starting move
     * @param mc    The column of the starting move
     */
    public static void floodFill(boolean[][] mines, char[][] board, int mr, int mc) {
        // Flood fill ~20 cells (1/5 of the board)
        int numWalks = rand.nextInt(20);
        Deque<int[]> dq = new ArrayDeque<>();
        dq.add(new int[] { mr, mc });

        // Directions for BFS
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        // Fill the starting cell and ensure it is safe
        mines[mr][mc] = false;
        board[mr][mc] = 'F';

        //  Run BFS and uncover 20 new unique squares
        while (!dq.isEmpty() && numWalks > 0) {
            int[] front = dq.pollLast();
            int r = front[0], c = front[1];

            for (int[] i : dirs) {
                int nr = r + i[0];
                int nc = c + i[1];

                if (nr >= 0 && nr < mines.length && nc >= 0 && nc <= mines[0].length && board[nr][nc] != 'F') {
                    mines[nr][nc] = false;
                    board[nr][nc] = 'F';
                    dq.add(new int[] { nr, nc });
                    numWalks--;
                }
            }
        }

        // Replace the filled cells with their respective numbers
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (board[i][j] == 'F') {
                    board[i][j] = uncover(i, j);
                }
            }
        }

    }

    /**
     * Determine the number of mines around a cell
     * @param my y-coordinate (row) of the move
     * @param mx x-coordinate (col) of the move
     * @return   Number of mines surrounding the selected cell
     */
    public static char uncover(int my, int mx) {
        int[] dirs = { -1, 0, 1 };
        int cnt = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int ny = my + dirs[i];
                int nx = mx + dirs[j];
                if (my + dirs[i] >= 0 && my + dirs[i] < r && mx + dirs[j] >= 0 && mx + dirs[j] < c) {
                    if (mines[ny][nx])
                        cnt++;
                }
            }
        }
        return (cnt + "").charAt(0);
    }
}