package com.example;

public class Test {
    public static void main(String[] args) {
        Game g = new Game(10, 20);
        g.init(3,3);
        char[][] b = g.getBoard();
        for(int i = 0; i < b.length; i++) {
            for(int j = 0; j < b[0].length; j++) {
                System.out.print(b[i][j] + " ");
            }
            System.out.println();
        }
    }
}
